package de.uni_stuttgart.it_rex.media.written.repository;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.Video;
import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;
import de.uni_stuttgart.it_rex.media.repository.written.VideoRepositoryExtended;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestSecurityConfiguration.class})
@Transactional
class VideoRepositoryExtendedTestIT {

  private static final String TEST_TITLE = " The Lord Of The Rings Trilogy (Limited Extended Edition)";
  private static final UUID TEST_UUID = java.util.UUID.randomUUID();
  private static final MIMETYPE TEST_MIMETYPE = MIMETYPE.VIDEO_MP4;
  private static final Integer TEST_WIDTH = 3840;
  private static final Integer TEST_HEIGHT = 2160;

  @Autowired
  VideoRepositoryExtended videoRepositoryExtended;

  @Test
  void saveAndFind() {
    Video lordOfTheRings = new Video();
    lordOfTheRings.setTitle(TEST_TITLE);
    lordOfTheRings.setUuid(TEST_UUID);
    lordOfTheRings.setMimeType(TEST_MIMETYPE);
    lordOfTheRings.setWidth(TEST_WIDTH);
    lordOfTheRings.setHeight(TEST_HEIGHT);

    videoRepositoryExtended.save(lordOfTheRings);
    Optional<Video> results = videoRepositoryExtended.findVideoByUuid(TEST_UUID);
    assertThat(results).isPresent();
    assertThat(results).contains(lordOfTheRings);
  }
}
