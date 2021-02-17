package de.uni_stuttgart.it_rex.media.domain.written;

import de.uni_stuttgart.it_rex.media.domain.written.enumeration.MIMETYPE;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ResourceTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();
  private static final MIMETYPE MIMETYPE_1 = MIMETYPE.AUDIO_MPEG;
  private static final MIMETYPE MIMETYPE_2 = MIMETYPE.VIDEO_MP4;

  @Test
  void equalsVerifier() {
    Resource resource1 = new Document();
    resource1.setId(FIRST_ID);
    resource1.setMimeType(MIMETYPE_1);

    Resource resource2 = new Document();
    resource2.setId(SECOND_ID);
    resource2.setMimeType(MIMETYPE_2);

    Resource resource3 = new Document();
    resource3.setId(FIRST_ID);
    resource3.setMimeType(MIMETYPE_1);

    assertEquals(resource1, resource1);
    assertEquals(resource1, resource3);
    assertNotEquals(resource1, resource2);
  }
}
