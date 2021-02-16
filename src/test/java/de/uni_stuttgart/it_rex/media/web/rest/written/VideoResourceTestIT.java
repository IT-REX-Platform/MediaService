package de.uni_stuttgart.it_rex.media.web.rest.written;

import com.jayway.jsonpath.JsonPath;
import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.config.written.MinioConfig;
import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import de.uni_stuttgart.it_rex.media.written.testutils.MinioContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class, MinioConfig.class})
class VideoResourceTestIT {
  private static final Integer MINIO_PORT = 9000;
  private final String LOG_MESSAGE =
      "hello.txt is successfully uploaded as object hello.txt to bucket 'videos'.";
  private Integer minioMappedPort;
  private String minioMappedHost;
  private String minioUrl;
  private String minioAccessKey;
  private String minioSecretKey;
  private DockerComposeContainer environment;

  @Autowired
  private VideoService videoService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MinioContainer minioContainer;

  @Test
  void contextLoads() {
    assertThat(videoService).isNotNull();
    assertThat(videoService.getFileValidatorService()).isNotNull();
    assertThat(videoService.getVideoRepository()).isNotNull();
    assertThat(videoService.getVideoRepository()).isNotNull();
  }

  @Test
  void uploadDownloadAndDeleteFile() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    String result = mockMvc.perform(multipart("/api/videos").file(file)).
        andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
    String id = JsonPath.read(result, "$.id");
    mockMvc.perform(get("/api/videos/" + id)).andExpect(status().isOk());
    mockMvc.perform(delete("/api/videos/" + id)).andExpect(status().isNoContent());
    mockMvc.perform(get("/api/videos/" + id)).andExpect(status().
        is4xxClientError());
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
    mockMvc.perform(multipart("/api/videos6969").file(file)).andExpect(status().is4xxClientError());
  }

  @Test
  void downloadNonExistingFile() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc.perform(get("/api/videos/" + UUID.randomUUID().toString())).andExpect(status().isNotFound());
  }
}
