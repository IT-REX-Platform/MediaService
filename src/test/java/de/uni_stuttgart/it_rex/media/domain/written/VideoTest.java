package de.uni_stuttgart.it_rex.media.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class VideoTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();
  private static final Integer FIRST_LENGTH = 42;
  private static final Integer SECOND_LENGTH = 69;
  private static final Integer FIRST_WIDTH = 23423;
  private static final Integer SECOND_WIDTH = 2999423;
  private static final Integer FIRST_HEIGHT = 2342123;
  private static final Integer SECOND_HEIGHT = 23345423;

  @Test
  public void equalsVerifier() {
    Video video1 = new Video();
    video1.setId(FIRST_ID);
    Video video2 = new Video();
    video2.setId(video1.getId());
    assertThat(video1).isEqualTo(video2);

    video1.setLength(FIRST_LENGTH);
    video1.setWidth(FIRST_WIDTH);
    video1.setHeight(FIRST_HEIGHT);
    video2.setId(SECOND_ID);
    video2.setLength(SECOND_LENGTH);
    video1.setWidth(SECOND_WIDTH);
    video1.setHeight(SECOND_HEIGHT);

    assertThat(video1).isNotEqualTo(video2);
    video1.setId(null);
    assertThat(video1).isNotEqualTo(video2);
  }
}

