package de.uni_stuttgart.it_rex.media.written.repository;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;
import de.uni_stuttgart.it_rex.media.domain.written.Video;
import de.uni_stuttgart.it_rex.media.repository.written.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestSecurityConfiguration.class})
@Transactional
class VideoRepositoryTestIT {

  private static final String TITLE = "The Lord of the Rings: The Fellowship of the Ring";
  private static final MIMETYPE TEST_MIMETYPE = MIMETYPE.VIDEO_MP4;
  private static final Integer WIDTH = 3840;
  private static final Integer HEIGHT = 2160;
  private static final Integer LENGTH = 10680;
  private static final LocalDate START_DATE = LocalDate.of(2001, Month.DECEMBER, 10);
  private static final LocalDate END_DATE = LocalDate.MAX;
  private static final Long COURSE_ID = 9L;
  private static final Long CHAPTER_ID = 99L;
  private static final Long UPLOADER_ID = 999L;

  @Autowired
  VideoRepository videoRepository;

  @Test
  void saveAndFind() {
    Video lordOfTheRings = new Video();
    lordOfTheRings.setTitle(TITLE);
    lordOfTheRings.setMimeType(TEST_MIMETYPE);
    lordOfTheRings.setWidth(WIDTH);
    lordOfTheRings.setHeight(HEIGHT);
    lordOfTheRings.setLength(LENGTH);
    lordOfTheRings.setStartDate(START_DATE);
    lordOfTheRings.setEndDate(END_DATE);
    lordOfTheRings.setCourseId(COURSE_ID);
    lordOfTheRings.setChapterId(CHAPTER_ID);
    lordOfTheRings.setUploaderId(UPLOADER_ID);

    UUID id = videoRepository.save(lordOfTheRings).getId();
    Optional<Video> result = videoRepository.findById(id);
    assertThat(result).isPresent();
    assertThat(result).contains(lordOfTheRings);
  }
}
