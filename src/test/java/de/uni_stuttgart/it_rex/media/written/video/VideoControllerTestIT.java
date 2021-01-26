package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.util.Assert;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@SpringBootTest(classes = {TestSecurityConfiguration.class})
class VideoControllerTestIT {
  private static int MINIO_PORT = 9000;
  private int mappedPort;
  private String mappedHost;
  private DockerComposeContainer environment;

  @Autowired
  private VideoStorageService videoStorageService;

  @Autowired
  private VideoController videoController;

  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  public void setUp(@Value("${minio.access-key}") final String accessKey,
                    @Value("${minio.secret-key}") final String secretKey) {
    environment = new DockerComposeContainer(new File("src/test/resources/docker/minio.yml")).
        withExposedService("minio", MINIO_PORT);
    environment.start();
    mappedPort = environment.getServicePort("minio", MINIO_PORT);
    mappedHost = environment.getServiceHost("minio", MINIO_PORT);
    videoStorageService.connect(String.format("http://%s:%d", mappedHost, mappedPort), accessKey, secretKey);
  }

  @AfterEach
  public void tearDown() {
    environment.stop();
  }

  @Test
  void uploadVideo() {
    //restTemplate.postForLocation()
    System.out.println(MINIO_PORT);
    Assert.isTrue(true);
  }
}