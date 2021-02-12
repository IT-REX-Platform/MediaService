package de.uni_stuttgart.it_rex.media.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AudioTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();

  @Test
  void equalsVerifier() {
    Audio audio1 = new Audio();
    audio1.setId(FIRST_ID);
    Audio audio2 = new Audio();
    audio2.setId(audio1.getId());
    assertThat(audio1).isEqualTo(audio2);
    audio2.setId(SECOND_ID);
    assertThat(audio1).isNotEqualTo(audio2);
    audio1.setId(null);
    assertThat(audio1).isNotEqualTo(audio2);
  }
}
