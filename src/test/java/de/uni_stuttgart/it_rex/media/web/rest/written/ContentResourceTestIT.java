package de.uni_stuttgart.it_rex.media.web.rest.written;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.written.Content;
import de.uni_stuttgart.it_rex.media.domain.written.Resource;
import de.uni_stuttgart.it_rex.media.domain.written.Video;
import de.uni_stuttgart.it_rex.media.domain.written.enumeration.MIMETYPE;
import de.uni_stuttgart.it_rex.media.repository.written.ContentRepository;
import de.uni_stuttgart.it_rex.media.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TestSecurityConfiguration.class})
class ContentResourceTestIT {

  private static final UUID ID_1 = UUID.randomUUID();
  private static final UUID ID_2 = UUID.randomUUID();
  private static final String TITLE_1 = "AAAAAAAAAA";
  private static final String TITLE_2 = "BBBBBBBBBB";
  private static final MIMETYPE MIMETYPE_1 = MIMETYPE.AUDIO_MPEG;
  private static final MIMETYPE MIMETYPE_2 = MIMETYPE.VIDEO_MP4;
  private static final LocalDate START_DATE_1 = LocalDate.MIN.plusDays(200);
  private static final LocalDate START_DATE_2 = LocalDate.MIN.plusDays(400);
  private static final LocalDate END_DATE_1 = LocalDate.MAX.minusDays(200);
  private static final LocalDate END_DATE_2 = LocalDate.MAX.minusDays(400);
  private static final UUID COURSE_ID_1 = UUID.randomUUID();
  private static final UUID COURSE_ID_2 = UUID.randomUUID();
  private static final UUID CHAPTER_ID_1 = UUID.randomUUID();
  private static final UUID CHAPTER_ID_2 = UUID.randomUUID();
  private static final UUID UPLOADER_ID_1 = UUID.randomUUID();
  private static final UUID UPLOADER_ID_2 = UUID.randomUUID();
  private static final UUID PREVIOUS_ID_1 = UUID.randomUUID();
  private static final UUID PREVIOUS_ID_2 = UUID.randomUUID();
  private static final UUID NEXT_ID_1 = UUID.randomUUID();
  private static final UUID NEXT_ID_2 = UUID.randomUUID();
  private static final Long LENGTH_1 = 42L;
  private static final Long LENGTH_2 = 69L;
  private static final Integer WIDTH_1 = 23423;
  private static final Integer WIDTH_2 = 2999423;
  private static final Integer HEIGHT_1 = 2342123;
  private static final Integer HEIGHT_2 = 23345423;

  @Autowired
  private ContentResource contentResource;

  @Autowired
  private ContentRepository contentRepository;

  @Autowired
  private MockMvc restContentMockMvc;

  private Content createContent1() {
    Video video = new Video();
    video.setTitle(TITLE_1);
    video.setMimeType(MIMETYPE_1);
    video.setStartDate(START_DATE_1);
    video.setEndDate(END_DATE_1);
    video.setCourseId(COURSE_ID_1);
    video.setChapterId(CHAPTER_ID_1);
    video.setUploaderId(UPLOADER_ID_1);
    video.setLength(LENGTH_1);
    video.setWidth(WIDTH_1);
    video.setHeight(HEIGHT_1);

    return video;
  }

  private Content createContent2() {
    Video video = new Video();
    video.setTitle(TITLE_2);
    video.setMimeType(MIMETYPE_2);
    video.setStartDate(START_DATE_2);
    video.setEndDate(END_DATE_2);
    video.setCourseId(COURSE_ID_2);
    video.setChapterId(CHAPTER_ID_2);
    video.setUploaderId(UPLOADER_ID_2);
    video.setLength(LENGTH_2);
    video.setWidth(WIDTH_2);
    video.setHeight(HEIGHT_2);

    return video;
  }

  private Content content1;
  private Content content2;

  @BeforeEach
  void init() {
    content1 = createContent1();
    content2 = createContent2();
  }

  @Test
  @Transactional
  void patchContent() throws Exception {
    Content toUpdate = contentRepository.saveAndFlush(content1);
    final UUID id = toUpdate.getId();

    Content expected = createContent2();
    expected.setId(id);
    expected.setTitle(TITLE_1);
    expected.setUploaderId(UPLOADER_ID_1);

    content2.setId(id);
    content2.setTitle(null);
    content2.setUploaderId(null);

    restContentMockMvc.perform(patch("/api/content").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(content2)))
        .andExpect(status().isOk());

    Optional<Content> updatedOptional = contentRepository.findById(id);

    assertTrue(updatedOptional.isPresent());
    Content updated = updatedOptional.get();

    assertEquals(expected.getId(), updated.getId());
    assertEquals(expected.getChapterId(), updated.getChapterId());
    assertEquals(expected.getTitle(), updated.getTitle());
    assertEquals(expected.getStartDate(), updated.getStartDate());
    assertEquals(expected.getEndDate(), updated.getEndDate());
    assertEquals(expected.getCourseId(), updated.getCourseId());
    assertEquals(expected.getChapterId(), updated.getChapterId());
    assertEquals(expected.getUploaderId(), updated.getUploaderId());
    assertEquals(MIMETYPE_1, ((Resource) updated).getMimeType());
    assertEquals(LENGTH_1, ((Video) updated).getLength());
    assertEquals(WIDTH_1, ((Video) updated).getWidth());
    assertEquals(HEIGHT_1, ((Video) updated).getHeight());
  }
}