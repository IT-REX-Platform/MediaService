package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.repository.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;
import de.uni_stuttgart.it_rex.media.service.mapper.VideoMapper;
import de.uni_stuttgart.it_rex.media.written.FileValidatorService;
import de.uni_stuttgart.it_rex.media.written.StorageFileNotFoundException;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class VideoServiceExtended extends VideoService {

    /**
     * One hour in seconds.
     */
    private static final Integer ONE_HOUR = 3600;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(VideoServiceExtended.class);

    /**
     * Transaction manager.
     */
    private PlatformTransactionManager transactionManager;

    /**
     * Service for validating files.
     */
    private FileValidatorService fileValidatorService;

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
     * @param vm the VideoMapper
     * @param vr the VideoRepository
     */
    @Autowired
    public VideoServiceExtended(
        final VideoRepository vr, final VideoMapper vm) {
        super(vr, vm);
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
        MinioClient minioClient = buildClient();
        try {
            // Make uploads bucket if not exist.
            boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(location.toString())
                    .build());
            if (!found) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(location.toString())
                        .build());
            } else {
                String bucketExistsLog = String
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
     * Store a video file.
     *
     * @param file The video file to store.
     * @return the video meta data
     */
    public VideoDTO store(
        final MultipartFile file) {
        fileValidatorService.validate(file);

        DefaultTransactionDefinition definition
            = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(
            TransactionDefinition.PROPAGATION_REQUIRED);
        definition.setIsolationLevel(
            TransactionDefinition.ISOLATION_DEFAULT);
        definition.setTimeout(ONE_HOUR);
        TransactionStatus status =
            transactionManager.getTransaction(definition);
        VideoDTO videoDTO = new VideoDTO();
        try {
            videoDTO.setTitle(file.getOriginalFilename());
            videoDTO.setLocation(this.rootLocation.toString());
            videoDTO = super.save(videoDTO);
            storeFile(videoDTO.getId(), file);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            if (videoDTO.getId() != null) {
                super.delete(videoDTO.getId());
                try {
                    deleteFile(videoDTO.getId());
                } catch (Exception ex) {
                    LOGGER.error(ex.getLocalizedMessage());
                }
            }
            LOGGER.error(e.getLocalizedMessage());
        }
        return videoDTO;
    }

    /**
     * Delete a video file from the system.
     *
     * @param videoId the id of the video file to remove.
     */
    @Override
    public void delete(final Long videoId) {
        Optional<VideoDTO> result = super.findOne(videoId);
        if (!result.isPresent()) {
            String videoMissingLog =
                String.format("There is no video with the id %d!", videoId);
            LOGGER.info(videoMissingLog);
            return;
        }
        VideoDTO videoDTO = result.get();

        DefaultTransactionDefinition definition =
            new DefaultTransactionDefinition();
        definition.setPropagationBehavior(
            TransactionDefinition.PROPAGATION_REQUIRED);
        definition.setIsolationLevel(
            TransactionDefinition.ISOLATION_DEFAULT);
        definition.setTimeout(ONE_HOUR);
        TransactionStatus status =
            transactionManager.getTransaction(definition);
        try {
            super.delete(videoId);
            deleteFile(videoDTO.getId());
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            if (videoDTO.getId() != null) {
                super.save(videoDTO);
            }
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    /**
     * Stores a file in Minio.
     *
     * @param videoId the id of the video
     * @param file    The video file to store.
     * @return the files location.
     */
    private String storeFile(final Long videoId, final MultipartFile file)
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
        MinioClient minioClient = buildClient();

        ObjectWriteResponse result = minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(rootLocation.toString())
                .object(videoId.toString())
                .stream(file.getInputStream(), -1, UNKNOWN_FILE_SIZE)
                .build());

        String uploadSuccessLog = String
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
    private void deleteFile(final Long id)
        throws IOException,
        InvalidKeyException,
        InvalidResponseException,
        InsufficientDataException,
        NoSuchAlgorithmException,
        ServerException,
        InternalException,
        XmlParserException,
        ErrorResponseException {
        MinioClient minioClient = buildClient();
        minioClient.removeObject(
            RemoveObjectArgs.builder().bucket(rootLocation.toString())
                .object(id.toString()).build());

        String uploadSuccessLog = String
            .format("The file with the id %s was successfully removed from %s.",
                id, rootLocation.toString());
        LOGGER.info(uploadSuccessLog);
    }

    /**
     * Getter.
     *
     * @return the transaction manager.
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Setter.
     *
     * @param newTransactionManager the transaction manager.
     */
    @Autowired
    public void setTransactionManager(
        final PlatformTransactionManager newTransactionManager) {
        this.transactionManager = newTransactionManager;
    }

    /**
     * Get the size of a stored file.
     *
     * @param filename Name of the file to stat
     * @return Length of file
     */
    public long getLength(final String filename) {

        MinioClient minioClient = buildClient();

        try {

            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(rootLocation.toString())
                    .object(filename)
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
    public Resource loadAsResource(final String filename, final long offset,
                                   final long length) {

        MinioClient minioClient = buildClient();

        // get object given the bucket and object name
        try {

            InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(rootLocation.toString())
                    .object(filename)
                    .offset(offset)
                    .length(length)
                    .build());

            // Read data from stream
            InputStreamResource
                resource = new InputStreamResource(stream, filename);

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
    @Autowired
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
    @Autowired
    public final void setMinioUrl(
        @Value("${minio.url}") final String newMinioUrl) {
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
    @Autowired
    public final void setAccessKey(
        @Value("${minio.access-key}") final String newAccessKey) {
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
    @Autowired
    public final void setSecretKey(
        @Value("${minio.secret-key}") final String newSecretKey) {
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
    @Autowired
    public final void setRootLocation(
        @Value("${minio.root-location}") final Path newLocation) {
        this.rootLocation = newLocation;
    }
}
