package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;
import de.uni_stuttgart.it_rex.media.written.testutils.UnwrapProxied;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@Transactional
@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class, VideoServiceMockConfig.class})
public class VideoStorageServiceTransactionTestIT {

  private static final Integer MINIO_PORT = 9000;

  private Integer minioMappedPort;
  private String minioMappedHost;
  private String minioUrl;
  private String minioAccessKey;
  private String minioSecretKey;
  private DockerComposeContainer environment;

  @Autowired
  private VideoStorageService videoStorageService;

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
      VideoStorageService videoStorageServiceUnwrapped =
          ((VideoStorageService) UnwrapProxied.unwrap(videoStorageService));
      videoStorageServiceUnwrapped.setMinioUrl(minioUrl);
      videoStorageServiceUnwrapped.setAccessKey(minioAccessKey);
      videoStorageServiceUnwrapped.setSecretKey(minioSecretKey);
      videoStorageService.makeBucket(videoStorageServiceUnwrapped.getRootLocation());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterAll
  public void tearDown() {
    environment.stop();
  }

  @Test
  void failDatabaseWrite() {
    MockMultipartFile emptyFile = new MockMultipartFile(
        "FailedMetatDataWrite",
        "FailedMetatDataWrite.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hihi".getBytes()
    );
    videoStorageService.store(emptyFile);
    List<VideoDTO> dtos = videoService.findAll();
    Optional<VideoDTO> result = dtos.stream().filter(dto
        -> dto.getTitle().equals("FailedMetatDataWrite.txt")).findFirst();
    assertThat(result).isEmpty();
  }

  @Test
  void deleteNotExisting() {
    assertThat(videoStorageService.delete(999999999L)).isNull();
  }
}
