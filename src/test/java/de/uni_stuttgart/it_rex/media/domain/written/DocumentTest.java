package de.uni_stuttgart.it_rex.media.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();

  @Test
  void equalsVerifier() {
    Document document1 = new Document();
    document1.setId(FIRST_ID);
    Document document2 = new Document();
    document2.setId(document1.getId());
    assertThat(document1).isEqualTo(document2);
    document2.setId(SECOND_ID);
    assertThat(document1).isNotEqualTo(document2);
    document1.setId(null);
    assertThat(document1).isNotEqualTo(document2);
  }
}
