package de.uni_stuttgart.it_rex.media.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class VideoTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();

  @Test
  public void equalsVerifier() {
    Video video1 = new Video();
    video1.setId(FIRST_ID);
    Video video2 = new Video();
    video2.setId(video1.getId());
    assertThat(video1).isEqualTo(video2);
    video2.setId(SECOND_ID);
    assertThat(video1).isNotEqualTo(video2);
    video1.setId(null);
    assertThat(video1).isNotEqualTo(video2);
  }
}
