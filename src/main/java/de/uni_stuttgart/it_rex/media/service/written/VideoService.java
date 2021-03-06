package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.domain.written.Video;
import de.uni_stuttgart.it_rex.media.repository.written.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.mapper.written.VideoMapper;
import de.uni_stuttgart.it_rex.media.service.written.events.FileCreatedEvent;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Service Implementation for managing {@link Video}.
 */
@Service
@Transactional
public class VideoService {
  /**
   * Logger.
   */
  private static final Logger LOGGER
      = LoggerFactory.getLogger(VideoService.class);

  /**
   * ApplicationEvent publisher.
   */
  private ApplicationEventPublisher applicationEventPublisher;

  /**
   * Service for validating files.
   */
  private FileValidatorService fileValidatorService;

  /**
   * Repository to save meta information.
   */
  private VideoRepository videoRepository;

  /**
   * Video mapper.
   */
  private VideoMapper videoMapper;

  /**
   * The url to minio.
   */
  private String minioUrl;

  /**
   * The access key of the minio instance.
   */
  private String accessKey;

  /**
   * The secreat key of the minio instance.
   */
  private String secretKey;

  /**
   * Name of the root bucket.
   */
  private Path rootLocation;

  /**
   * If the input file size is unknown it is recommended to use a high number.
   * Source: https://docs.min.io/docs/java-client-api-reference.html#putObject
   */
  private static final int UNKNOWN_FILE_SIZE = 10485760;

  /**
   * Constructor.
   *
   * @param newApplicationEventPublisher the event publisher
   * @param newFileValidatorService      the file validator
   * @param newVideoRepository           the video repository
   * @param newVideoMapper               the video mapper
   * @param newMinioUrl                  the minio url
   * @param newAccessKey                 the access key
   * @param newSecretKey                 the secret key
   * @param newLocation                  the location
   */
  @Autowired
  public VideoService(
      final ApplicationEventPublisher newApplicationEventPublisher,
      final FileValidatorService newFileValidatorService,
      final VideoRepository newVideoRepository,
      final VideoMapper newVideoMapper,
      @Value("${minio.url}") final String newMinioUrl,
      @Value("${minio.access-key}") final String newAccessKey,
      @Value("${minio.secret-key}") final String newSecretKey,
      @Value("${minio.root-location}") final Path newLocation) {
    this.applicationEventPublisher = newApplicationEventPublisher;
    this.fileValidatorService = newFileValidatorService;
    this.videoRepository = newVideoRepository;
    this.videoMapper = newVideoMapper;
    this.minioUrl = newMinioUrl;
    this.accessKey = newAccessKey;
    this.secretKey = newSecretKey;
    this.rootLocation = newLocation;
  }

  /**
   * Initializes the VideoStorage.
   */
  @PostConstruct
  private void init() {
    makeBucket(this.rootLocation);
  }

  /**
   * Creates a bucket in minio in the specified location
   * if it doesn't already exist.
   *
   * @param location the location
   */
  public void makeBucket(final Path location) {
    final MinioClient minioClient = buildClient();
    try {
      // Make uploads bucket if not exist.
      final boolean found = minioClient.bucketExists(
          BucketExistsArgs.builder().bucket(location.toString())
              .build());
      if (!found) {
        minioClient.makeBucket(
            MakeBucketArgs.builder().bucket(location.toString())
                .build());
      } else {
        final String bucketExistsLog = String
            .format("Bucket %s already exists.", location);
        LOGGER.info(bucketExistsLog);
      }
    } catch (InvalidKeyException
        | NoSuchAlgorithmException
        | IOException
        | MinioException e) {
      LOGGER.error(e.getLocalizedMessage());
    }
  }

  /**
   * This method creates a MinioClient that this VideoStorage connects to.
   *
   * @return the MinioClient
   */
  private MinioClient buildClient() {
    return MinioClient.builder()
        .endpoint(this.minioUrl)
        .credentials(this.accessKey, this.secretKey)
        .build();
  }

  /**
   * Saves a video entity to the videoRepository.
   *
   * @param video the video entity
   * @return the video entity
   */
  private Video save(final Video video) {
    LOGGER.debug("Request to save Video : {}", video);
    return videoRepository.save(video);
  }

  /**
   * Store a video file.
   *
   * @param videoFile  The video file to store.
   * @param courseUuid Course ID as UUID.
   * @return the video meta data
   */
  @Transactional(rollbackFor = {Exception.class})
  public Video store(
      final MultipartFile videoFile,
      final UUID courseUuid)
      throws IOException,
      InvalidKeyException,
      InvalidResponseException,
      InsufficientDataException,
      NoSuchAlgorithmException,
      ServerException,
      ErrorResponseException,
      XmlParserException,
      InternalException {
    fileValidatorService.validate(videoFile);

    final Video video = this.save(new Video());

    storeFile(video.getId(), videoFile);
    applicationEventPublisher.publishEvent(
        new FileCreatedEvent(this, video.getId()));

    video.setLength(this.getLength(video.getId()));
    video.setTitle(videoFile.getOriginalFilename());
    video.setCourseId(courseUuid);
    this.save(video);

    return video;
  }

  /**
   * Rolls back the creation of files in minio.
   *
   * @param fileCreatedEvent the event
   */
  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  protected void rollBackFileStorage(
      final FileCreatedEvent fileCreatedEvent) {
    try {
      deleteFromMinio(fileCreatedEvent.getId());
    } catch (Exception e) {
      LOGGER.error(e.getLocalizedMessage());
    }
  }

  /**
   * Deletes a video entity from the videoRepository by id.
   *
   * @param videoId the video id
   * @return the video id
   */
  private UUID delete(final UUID videoId) {
    LOGGER.debug("Request to delete Video : {}", videoId);
    videoRepository.deleteById(videoId);
    return videoId;
  }

  /**
   * Delete a video file from the system.
   *
   * @param videoId the id of the video file to remove.
   */
  @Transactional
  public void deleteFile(final UUID videoId)
      throws
      InvalidResponseException,
      InvalidKeyException,
      NoSuchAlgorithmException,
      ServerException,
      ErrorResponseException,
      XmlParserException,
      InsufficientDataException,
      InternalException,
      IOException {
    this.delete(videoId);
    deleteFromMinio(videoId);
  }

  /**
   * Stores a file in Minio.
   *
   * @param videoId the id of the video
   * @param file    The video file to store.
   * @return the files location.
   */
  private String storeFile(final UUID videoId, final MultipartFile file)
      throws
      IOException,
      ServerException,
      InsufficientDataException,
      InternalException,
      InvalidResponseException,
      InvalidKeyException,
      NoSuchAlgorithmException,
      XmlParserException,
      ErrorResponseException {
    final MinioClient minioClient = buildClient();

    final ObjectWriteResponse result = minioClient.putObject(
        PutObjectArgs.builder()
            .bucket(rootLocation.toString())
            .object(videoId.toString())
            .stream(file.getInputStream(), -1, UNKNOWN_FILE_SIZE)
            .build());

    final String uploadSuccessLog = String
        .format(
            "%s is successfully uploaded as object %s to bucket '%s'.",
            file.getOriginalFilename(), result.object(),
            result.bucket());
    LOGGER.info(uploadSuccessLog);

    return result.bucket();
  }

  /**
   * Removes a file from Minio.
   *
   * @param id The video file to delete.
   */
  private void deleteFromMinio(final UUID id)
      throws IOException,
      InvalidKeyException,
      InvalidResponseException,
      InsufficientDataException,
      NoSuchAlgorithmException,
      ServerException,
      InternalException,
      XmlParserException,
      ErrorResponseException {
    final MinioClient minioClient = buildClient();
    minioClient.removeObject(
        RemoveObjectArgs.builder().bucket(rootLocation.toString())
            .object(id.toString()).build());

    final String uploadSuccessLog = String
        .format("The file with the id %s was successfully removed from %s.",
            id, rootLocation.toString());
    LOGGER.info(uploadSuccessLog);
  }

  /**
   * Method finds all videos corresponding to given filters.
   *
   * @param courseId optional filter parameter course ID.
   * @return all videos after filtering.
   */
  @Transactional(readOnly = true)
  public List<Video> findAllVideosOfACourse(final UUID courseId) {
    LOGGER.info("Find videos of course: {}", courseId);
    return videoRepository.findAllByCourseIdOrderByTitleAsc(courseId);
  }

  /**
   * Finds a Video by id.
   *
   * @param id the id
   * @return the video
   */
  @Transactional(readOnly = true)
  public Optional<Video> findById(final UUID id) {
    return videoRepository.findById(id);
  }

  /**
   * Finds all Videos by id.
   *
   * @param ids the ids
   * @return the videos
   */
  @Transactional(readOnly = true)
  public List<Video> findAllWithIds(final Iterable<UUID> ids) {
    return videoRepository.findAllById(ids);
  }

  /**
   * Get the size of a stored file.
   *
   * @param id id of the file to stat
   * @return Length of file
   */
  public long getLength(final UUID id) {

    final MinioClient minioClient = buildClient();

    try {

      StatObjectResponse stat = minioClient.statObject(
          StatObjectArgs.builder()
              .bucket(rootLocation.toString())
              .object(id.toString())
              .build());

      return stat.size();

    } catch (InvalidKeyException
        | NoSuchAlgorithmException
        | MinioException
        | IOException e) {
      LOGGER.error(e.getLocalizedMessage());
    }

    return 0L;
  }

  /**
   * Get the contents of a stored file.
   *
   * @param filename Name of file to load
   * @param offset   Starting offset to read from
   * @param length   How many bytes to read
   * @return *length* bytes of requested file starting from *offset* if file
   * exists, null otherwise
   */
  public Resource loadAsResource(final UUID filename, final long offset,
                                 final long length) {

    final MinioClient minioClient = buildClient();

    // get object given the bucket and object name
    try {

      final InputStream stream = minioClient.getObject(
          GetObjectArgs.builder()
              .bucket(rootLocation.toString())
              .object(filename.toString())
              .offset(offset)
              .length(length)
              .build());

      // Read data from stream
      final InputStreamResource
          resource = new InputStreamResource(stream, filename.toString());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);
      }

    } catch (InvalidKeyException
        | NoSuchAlgorithmException
        | MinioException
        | IOException e) {
      LOGGER.error(e.getLocalizedMessage());
    }

    return null;
  }

  /**
   * Update a video without overwriting it.
   *
   * @param video the entity to use to update a created entity.
   * @return the persisted entity.
   */
  public Video patch(final Video video) {
    LOGGER.debug("Request to update Video : {}", video);
    final Optional<Video> videoStored = videoRepository.findById(video.getId());

    if (videoStored.isEmpty()) {
      return null;
    }

    final Video videoStoredEntity = videoStored.get();
    final Video videoSanitized = sanitizeVideoPatch(video);
    videoMapper.updateVideoFromVideo(videoSanitized, videoStoredEntity);
    return videoRepository.save(videoStoredEntity);
  }

  /**
   * Method removes unwanted changes to video and content entities.
   *
   * @param video the entity to use to update a created video/content entity.
   * @return the sanitized entity.
   */
  private Video sanitizeVideoPatch(final Video video) {
    final Video videoSanitized = new Video();

    if (video.getTitle() != null && !video.getTitle().equals("")) {
        videoSanitized.setTitle(video.getTitle());
    }
    if (video.getStartDate() != null) {
      videoSanitized.setStartDate(video.getStartDate());
    }
    if (video.getEndDate() != null) {
      videoSanitized.setEndDate(video.getEndDate());
    }
    if (video.getChapterId() != null) {
      videoSanitized.setChapterId(video.getChapterId());
    }

    return videoSanitized;
  }

  /**
   * Getter.
   *
   * @return the fileValidatorService
   */
  public FileValidatorService getFileValidatorService() {
    return fileValidatorService;
  }

  /**
   * Setter.
   *
   * @param newFileValidatorService the fileValidatorService
   */
  public void setFileValidatorService(
      final FileValidatorService newFileValidatorService) {
    this.fileValidatorService = newFileValidatorService;
  }

  /**
   * Getter.
   *
   * @return the url where minio can be found
   */
  public final String getMinioUrl() {
    return minioUrl;
  }

  /**
   * Setter.
   *
   * @param newMinioUrl the url where minio can be found
   */
  public final void setMinioUrl(final String newMinioUrl) {
    this.minioUrl = newMinioUrl;
  }

  /**
   * Getter.
   *
   * @return the minio access key
   */
  public final String getAccessKey() {
    return accessKey;
  }

  /**
   * Setter.
   *
   * @param newAccessKey the minio access key
   */
  public final void setAccessKey(final String newAccessKey) {
    this.accessKey = newAccessKey;
  }

  /**
   * Getter.
   *
   * @return the minio secret key
   */
  public final String getSecretKey() {
    return secretKey;
  }

  /**
   * Setter.
   *
   * @param newSecretKey the minio secret key
   */
  public final void setSecretKey(final String newSecretKey) {
    this.secretKey = newSecretKey;
  }

  /**
   * Getter.
   *
   * @return the root bucket of minio
   */
  public final Path getRootLocation() {
    return rootLocation;
  }

  /**
   * Setter.
   *
   * @param newLocation the root bucket of minio
   */
  public final void setRootLocation(final Path newLocation) {
    this.rootLocation = newLocation;
  }

  /**
   * Getter.
   *
   * @return the applicationEventPublisher
   */
  public ApplicationEventPublisher getApplicationEventPublisher() {
    return applicationEventPublisher;
  }

  /**
   * Setter.
   *
   * @param newApplicationEventPublisher the applicationEventPublisher
   */
  public void setApplicationEventPublisher(
      final ApplicationEventPublisher newApplicationEventPublisher) {
    this.applicationEventPublisher = newApplicationEventPublisher;
  }

  /**
   * Getter.
   *
   * @return the videoRepository
   */
  public VideoRepository getVideoRepository() {
    return videoRepository;
  }

  /**
   * Setter.
   *
   * @param newVideoRepository the videoRepository
   */
  public void setVideoRepository(final VideoRepository newVideoRepository) {
    this.videoRepository = newVideoRepository;
  }

  /**
   * Getter.
   *
   * @return the videoMapper
   */
  public VideoMapper getVideoMapper() {
    return videoMapper;
  }

  /**
   * Setter.
   *
   * @param newVideoMapper the videoMapper
   */
  public void setVideoMapper(final VideoMapper newVideoMapper) {
    this.videoMapper = newVideoMapper;
  }
}
