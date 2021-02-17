package de.uni_stuttgart.it_rex.media.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AudioTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();
  private static final Long FIRST_LENGTH = 42L;
  private static final Long SECOND_LENGTH = 69L;

  @Test
  void equalsVerifier() {
    Audio audio1 = new Audio();
    audio1.setId(FIRST_ID);
    audio1.setLength(FIRST_LENGTH);

    Audio audio2 = new Audio();
    audio2.setId(SECOND_ID);
    audio2.setLength(SECOND_LENGTH);

    Audio audio3 = new Audio();
    audio3.setId(FIRST_ID);
    audio3.setLength(FIRST_LENGTH);

    assertEquals(audio1, audio1);
    assertEquals(audio1, audio3);
    assertNotEquals(audio1, audio2);
  }
}
