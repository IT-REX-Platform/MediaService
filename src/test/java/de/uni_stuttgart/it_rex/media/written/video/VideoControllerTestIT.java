package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setRemoveAssertJRelatedElementsFromStackTrace;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class})
class VideoControllerTestIT {
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
  private VideoController videoController;

  @Autowired
  private WebApplicationContext webApplicationContext;

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
    videoStorageService.setMinioUrl(this.minioUrl);
    videoStorageService.setAccessKey(minioAccessKey);
    videoStorageService.setSecretKey(minioSecretKey);
    videoStorageService.makeBucket(videoStorageService.getRootLocation());
  }

  @AfterAll
  public void tearDown() {
    environment.stop();
  }

  @Test
  void contextLoads() {
    assertThat(videoController).isNotNull();
    assertThat(videoStorageService).isNotNull();
    assertThat(environment).isNotNull();
    assertThat(webApplicationContext).isNotNull();
    assertThat(minioMappedHost).isNotNull();
    assertThat(minioMappedPort).isNotNull();
    assertThat(minioUrl).isNotNull();
  }

  @Test
  void uploadFile() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc.perform(multipart("/api/videos/upload").file(file)).andExpect(status().isOk());
  }

  @Test
  void uploadFileToWrongEndpoint() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc.perform(multipart("/api/videos/upload6969").file(file)).andExpect(status().is4xxClientError());
  }

  @Test
  void downloadFile() throws Exception {
      MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
      mockMvc.perform(get("/api/videos/download/hello.txt")).andExpect(status().isOk());
  }

  @Test
  void downloadNonExistingFile() throws Exception {
      MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
      mockMvc.perform(get("/api/videos/download/hello1.txt")).andExpect(status().isNotFound());
  }
}
