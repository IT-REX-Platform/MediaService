package de.uni_stuttgart.it_rex.media.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTest {
  private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();

  @Test
  public void equalsVerifier() {
    Image image1 = new Image();
    image1.setId(FIRST_ID);
    Image image2 = new Image();
    image2.setId(image1.getId());
    assertThat(image1).isEqualTo(image2);
    image2.setId(SECOND_ID);
    assertThat(image1).isNotEqualTo(image2);
    image1.setId(null);
    assertThat(image1).isNotEqualTo(image2);
  }
}
