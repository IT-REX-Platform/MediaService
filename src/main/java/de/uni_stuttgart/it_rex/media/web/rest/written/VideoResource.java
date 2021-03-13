package de.uni_stuttgart.it_rex.media.web.rest.written;

import de.uni_stuttgart.it_rex.media.domain.written.Video;
import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import de.uni_stuttgart.it_rex.media.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
@RequestMapping("/api")
public class VideoResource {

  /**
   * Logger.
   */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(VideoResource.class);
  /**
   * Service for storing videos.
   */
  private final VideoService videoService;
  /**
   * The application name.
   */
  private String applicationName;

  /**
   * Constructor.
   *
   * @param vss                VideoStorageService.
   * @param name application name.
   */
  @Autowired
  public VideoResource(final VideoService vss,
                       @Value("${jhipster.clientApp.name}") final String name) {
    this.videoService = vss;
    this.applicationName = name;
  }

  /**
   * Upload a video to the system.
   * The video file is stored in a file storage
   * while metadata is persisted in an RDBMS.
   *
   * @param videoFile          The video file to upload.
   * @param courseId           Course ID as String.
   * @param redirectAttributes The redirect attributes.
   * @return the filename and id
   */
  @PostMapping("/videos")
  public ResponseEntity<Video> uploadVideo(
      @RequestPart("videoFile") final MultipartFile videoFile,
      @RequestPart("courseId") final String courseId,
      final RedirectAttributes redirectAttributes)
      throws URISyntaxException,
      IOException,
      InvalidResponseException,
      InvalidKeyException,
      NoSuchAlgorithmException,
      ServerException,
      InternalException,
      XmlParserException,
      InsufficientDataException,
      ErrorResponseException {
    UUID courseUuid;
    try {
        courseUuid = UUID.fromString(courseId);
    } catch (Exception e) {
        throw new BadRequestAlertException(
                "Invalid course ID",
                ENTITY_NAME,
                "invalidId");
    }

    Video result = videoService.store(videoFile, courseUuid);
    String logMessage =
        String.format("A video with the id %s was successfully created!",
            result.getId());
    LOGGER.info(logMessage);
    redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + videoFile.getOriginalFilename() + "!");

    return ResponseEntity.created(new URI("/api/videos/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(this.getApplicationName(),
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

    /**
   * Delete a video from the system.
   *
   * @param id                 of the video file to delete.
   * @param redirectAttributes redirectAttributes
   * @return the filename and id
   */
  @DeleteMapping("/videos/{id:.+}")
  public ResponseEntity<Void> deleteVideo(
      @PathVariable final UUID id,
      final RedirectAttributes redirectAttributes)
      throws
      IOException,
      InvalidResponseException,
      InvalidKeyException,
      NoSuchAlgorithmException,
      ServerException,
      InternalException,
      XmlParserException,
      InsufficientDataException,
      ErrorResponseException {
    videoService.deleteFile(id);

    redirectAttributes.addFlashAttribute(
        "message", "You successfully deleted " + id + "!");

    return ResponseEntity.noContent().headers(
        HeaderUtil.createEntityDeletionAlert(
            this.getApplicationName(),
            true, ENTITY_NAME, id.toString())).build();
  }

  /**
   * {@code GET  /videos} : get all the videos of a Course.
   *
   * @param courseId Course ID.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   * list of videos in body.
   */
  @GetMapping("/videos")
  public List<Video> findAllVideosOfACourse(
    @RequestParam("course_id") final UUID courseId) {
    LOGGER.debug("REST request to get all Videos");
    return videoService.findAllVideosOfACourse(courseId);
  }

  /**
   * Download a video from the system.
   *
   * @param id      of the video file to download.
   * @param headers The request headers.
   * @return A ResponseEntity to stream the file to the client.
   */
  @GetMapping("/videos/{id:.+}")
  public ResponseEntity<Resource> downloadVideo(
      @PathVariable final UUID id,
      @RequestHeader final HttpHeaders headers) {

    HttpHeaders responseHeaders = new HttpHeaders();

    long chosenFileLength = videoService.getLength(id);

    if (chosenFileLength == 0L) {
      return ResponseEntity.notFound().headers(responseHeaders).build();
    }

    HttpRange range;

    try {
      range = headers.getRange().get(0);
    } catch (IndexOutOfBoundsException e) {
      range = HttpRange.parseRanges("bytes=0-").get(0);
    }

    long start = range.getRangeStart(chosenFileLength);
    long end = range.getRangeEnd(chosenFileLength);
    long length = end - start + 1;

    Resource file = videoService
        .loadAsResource(id, start, length);

    if (file == null) {
      return ResponseEntity.notFound().headers(responseHeaders).build();
    }

    responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION,
        "inline; filename=\"" + id + "\"");
    responseHeaders.add("Content-Type", "video/mp4");

    return ResponseEntity.ok().headers(responseHeaders).body(file);
  }

  /**
   * {@code PATCH  /videos} : Patches an existing video.
   *
   * @param video The video to patch
   * @return the {@link ResponseEntity} with status {@code 200 (OD)} and with
   * body of the patched video,
   * or with status {@code 400 (Bad Request)} if the video ID is not valid,
   * or with status {@code 500 (Internal Server Error)} if the video
   * couldn't be patched.
   */
  @PatchMapping("/videos")
  public ResponseEntity<Video> patchVideo(@RequestBody final Video video) {
    LOGGER.debug("REST request to patch a Video");
    if (video.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }

    final Video result = videoService.patch(video);
    return ResponseEntity.ok()
            .headers(HeaderUtil
                    .createEntityUpdateAlert(this.getApplicationName(),
                    true, ENTITY_NAME, result.getId().toString()))
            .body(result);
  }

  /**
   * Getter.
   *
   * @return the application name.
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Setter.
   *
   * @param newApplicationName the application name.
   */
  public void setApplicationName(final String newApplicationName) {
    this.applicationName = newApplicationName;
  }
}
