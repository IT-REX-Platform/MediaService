package de.uni_stuttgart.it_rex.media.web.rest.written;

import com.jayway.jsonpath.JsonPath;
import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.config.written.MinioConfig;
import de.uni_stuttgart.it_rex.media.domain.written.Video;
import de.uni_stuttgart.it_rex.media.repository.written.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import de.uni_stuttgart.it_rex.media.util.written.VideoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestInstance(PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(classes = {TestSecurityConfiguration.class, MinioConfig.class})
class VideoResourceTestIT {

  private static final UUID COURSE_ID = UUID.randomUUID();

  @Autowired
  private VideoService videoService;

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc restCourseMockMvc;

  @Test
  void contextLoads() {
    assertThat(videoService).isNotNull();
    assertThat(videoService.getFileValidatorService()).isNotNull();
    assertThat(videoService.getVideoRepository()).isNotNull();
    assertThat(videoService.getVideoRepository()).isNotNull();
  }

  @Test
  void uploadDownloadAndDeleteFile() throws Exception {
    MockMultipartFile videoFile = new MockMultipartFile(
      "videoFile",
      "videoFile.txt",
      MediaType.TEXT_PLAIN_VALUE,
      "video_data".getBytes()
    );
    MockMultipartFile courseId = new MockMultipartFile(
      "courseId",
      "courseId.txt",
      MediaType.TEXT_PLAIN_VALUE,
      "6ec0bd7f-11c0-43da-975e-2a8ad9ebae0b".getBytes()
    );

    String result = restCourseMockMvc.perform(
      multipart("/api/videos")
        .file(videoFile)
        .file(courseId))
      .andExpect(status().isCreated())
      .andReturn().getResponse().getContentAsString();

    String id = JsonPath.read(result, "$.id");
    restCourseMockMvc.perform(get("/api/videos/" + id)).andExpect(status().isPartialContent());
    restCourseMockMvc.perform(delete("/api/videos/" + id)).andExpect(status().isNoContent());
    restCourseMockMvc.perform(get("/api/videos/" + id)).andExpect(status().is4xxClientError());
  }

  @Test
  void uploadVideoWithInvalidCourseId() throws Exception {
    MockMultipartFile videoFile = new MockMultipartFile(
      "videoFile",
      "videoFile.txt",
      MediaType.TEXT_PLAIN_VALUE,
      "video_data".getBytes()
    );
    MockMultipartFile courseId = new MockMultipartFile(
      "courseId",
      "courseId.txt",
      MediaType.TEXT_PLAIN_VALUE,
      "invalid_course_ID".getBytes()
    );

    MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
      .perform(multipart("/api/videos")
        .file(videoFile)
        .file(courseId))
      .andExpect(status().is4xxClientError());
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

  @Test
  void getByCourseId() throws Exception {
    List<Video> videos = VideoUtil.createVideos(2);
    videos.forEach(video -> video.setCourseId(COURSE_ID));
    videos = videoRepository.saveAll(videos);

    final List<Video> result = videoService.findAllVideosOfACourse(COURSE_ID);
    assertTrue(result.containsAll(videos));
  }
}
