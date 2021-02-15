package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.service.written.StorageException;
import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import de.uni_stuttgart.it_rex.media.written.testutils.UnwrapProxied;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class})
class VideoServiceTestIT {

  private static final Integer MINIO_PORT = 9000;
  private static final Path MAGIC_BUCKET = Paths.get("wizard-hat");
  private static final String LOG_MESSAGE1 = String.format("Bucket %s already exists.", MAGIC_BUCKET);
  private static final String EXCEPTION_MESSAGE = "Failed to store empty file!";

  private Integer minioMappedPort;
  private String minioMappedHost;
  private String minioUrl;
  private String minioAccessKey;
  private String minioSecretKey;
  private DockerComposeContainer environment;

  @Autowired
  private VideoService videoService;

  @BeforeAll
  public void setUp(@Value("${minio.access-key}") final String accessKey,
                    @Value("${minio.secret-key}") final String secretKey) {
    minioAccessKey = accessKey;
    minioSecretKey = secretKey;
    // The with ".withLocalCompose(true)" is needed to use the local installation of docker-compose
    environment = new DockerComposeContainer(new File("src/test/resources/docker/minio.yml")).
        withExposedService("minio", MINIO_PORT).withLocalCompose(true);
    environment.start();
    minioMappedPort = environment.getServicePort("minio", MINIO_PORT);
    minioMappedHost = environment.getServiceHost("minio", MINIO_PORT);
    minioUrl = String.format("http://%s:%d", minioMappedHost, minioMappedPort);
    try {
      VideoService videoServiceUnwrapped =
          ((VideoService) UnwrapProxied.unwrap(videoService));
      videoServiceUnwrapped.setMinioUrl(minioUrl);
      videoServiceUnwrapped.setAccessKey(minioAccessKey);
      videoServiceUnwrapped.setSecretKey(minioSecretKey);
      videoServiceUnwrapped.makeBucket(videoServiceUnwrapped.getRootLocation());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterAll
  public void tearDown() {
    environment.stop();
  }

  @Test
  void contextLoads() {
    assertThat(videoService).isNotNull();
    assertThat(environment).isNotNull();
    assertThat(minioMappedHost).isNotNull();
    assertThat(minioMappedPort).isNotNull();
    assertThat(minioUrl).isNotNull();
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

    Exception storageException = assertThrows(StorageException.class, () ->
        videoService.store(emptyFile));
    assertThat(storageException.getMessage()).isEqualTo(EXCEPTION_MESSAGE);
  }
}