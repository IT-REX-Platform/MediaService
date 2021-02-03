package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;
import de.uni_stuttgart.it_rex.media.written.StorageException;
import de.uni_stuttgart.it_rex.media.written.testutils.UnwrapProxied;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;

@Transactional
@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class, VideoServiceExtendedTransactionConfig.class})
class VideoServiceExtendedTransactionTestIT {

  private static final Integer MINIO_PORT = 9000;
  private static final Long MAGIC_NON_EXISTING_ID = 999999999L;

  private Integer minioMappedPort;
  private String minioMappedHost;
  private String minioUrl;
  private String minioAccessKey;
  private String minioSecretKey;
  private DockerComposeContainer environment;

  @Autowired
  private VideoServiceExtended videoServiceExtended;

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
      VideoServiceExtended videoServiceExtendedUnwrapped =
          ((VideoServiceExtended) UnwrapProxied.unwrap(videoServiceExtended));
      videoServiceExtendedUnwrapped.setMinioUrl(minioUrl);
      videoServiceExtendedUnwrapped.setAccessKey(minioAccessKey);
      videoServiceExtendedUnwrapped.setSecretKey(minioSecretKey);
      videoServiceExtended.makeBucket(videoServiceExtendedUnwrapped.getRootLocation());
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
    VideoService videoService = Mockito.mock(VideoService.class);
    Mockito.when(videoService.save(any(VideoDTO.class))).thenCallRealMethod().
        thenThrow(StorageException.class);

    MockMultipartFile emptyFile = new MockMultipartFile(
        "FailedMetatDataWrite",
        "FailedMetatDataWrite.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hihi".getBytes()
    );
    try {
      videoServiceExtended.store(emptyFile);
    } catch (Exception e) {
      // Empty because we expect an exception to be thrown.
    }
    List<VideoDTO> dtos = videoService.findAll();
    Optional<VideoDTO> result = dtos.stream().filter(dto
        -> dto.getTitle().equals("FailedMetatDataWrite.txt")).findFirst();
    assertThat(result).isEmpty();
  }

  @Test
  void deleteNotExisting() {
    assertThrows(EmptyResultDataAccessException.class,
        () -> videoServiceExtended.deleteFile(MAGIC_NON_EXISTING_ID));
  }
}
