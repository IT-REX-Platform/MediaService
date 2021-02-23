package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.config.written.MinioConfig;
import de.uni_stuttgart.it_rex.media.written.testutils.MinioContainer;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class, MinioConfig.class})
class VideoServiceTestIT {

  private static final Path MAGIC_BUCKET = Paths.get("wizard-hat");
  private static final String LOG_MESSAGE1 = String.format("Bucket %s already exists.", MAGIC_BUCKET);
  private static final String EXCEPTION_MESSAGE = "Failed to store empty file!";
  private static final UUID NOT_EXISTING_ID = UUID.randomUUID();
  private static final Long MAGIC_START = 42L;
  private static final Long MAGIC_LENGTH = 300L;

  private static final String MINIO_URL = "https://bit.ly/3on6jNe";
  private static final String ACCESS_KEY = "MagicAccessKey";
  private static final String SECRET_KEY = "MagicSecretKey";
  private static final String ROOT_LOCATION = "wizard-hat";

  @Autowired
  private FileValidatorService fileValidatorService;

  @Autowired
  private MinioContainer minioContainer;

  @Autowired
  private VideoService videoService;

  @Test
  void contextLoads() {
    assertThat(videoService).isNotNull();
    assertThat(videoService.getApplicationEventPublisher()).isNotNull();
    assertThat(videoService.getFileValidatorService()).isNotNull();
    assertThat(videoService.getVideoRepository()).isNotNull();
    assertThat(videoService.getVideoMapper()).isNotNull();
  }

  @Test
  void makeAlreadyExistingBucket() {
    LogCaptor logCaptor = LogCaptor.forClass(VideoService.class);
    videoService.makeBucket(MAGIC_BUCKET);
    videoService.makeBucket(MAGIC_BUCKET);
    assertThat(logCaptor.getInfoLogs()).containsExactly(LOG_MESSAGE1);
  }

  @Test
  void storeEmptyFile() {
    MockMultipartFile emptyFile = new MockMultipartFile(
        "empty",
        "empty.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "".getBytes()
    );

    UUID zeroUuid = new UUID( 0 , 0 );

    Exception storageException = assertThrows(StorageException.class, () ->
        videoService.store(emptyFile, zeroUuid));
    assertThat(storageException.getMessage()).isEqualTo(EXCEPTION_MESSAGE);
  }

  @Test
  void loadNotExisting() {
    assertNull(videoService.loadAsResource(NOT_EXISTING_ID, MAGIC_START, MAGIC_LENGTH));
  }

  @Test
  void getMinioUrl() {
    videoService.setMinioUrl(MINIO_URL);
    assertThat(videoService.getMinioUrl()).isEqualTo(MINIO_URL);
  }

  @Test
  void getAccessKey() {
    videoService.setAccessKey(ACCESS_KEY);
    assertThat(videoService.getAccessKey()).isEqualTo(ACCESS_KEY);
  }

  @Test
  void getSecretKey() {
    videoService.setSecretKey(SECRET_KEY);
    assertThat(videoService.getSecretKey()).isEqualTo(SECRET_KEY);
  }

  @Test
  void getRootLocation() {
    videoService.setSecretKey(ROOT_LOCATION);
    assertThat(videoService.getSecretKey()).isEqualTo(ROOT_LOCATION);
  }

  @Test
  void getFileValidatorService() {
    videoService.setFileValidatorService(fileValidatorService);
    assertThat(videoService.getFileValidatorService())
        .isEqualTo(fileValidatorService);
  }
}