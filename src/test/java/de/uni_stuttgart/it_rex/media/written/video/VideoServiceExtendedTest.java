package de.uni_stuttgart.it_rex.media.written.video;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VideoServiceExtendedTest {
  private static final String MINIO_URL = "https://bit.ly/3on6jNe";
  private static final String ACCESS_KEY = "MagicAccessKey";
  private static final String SECRET_KEY = "MagicSecretKey";
  private static final String ROOT_LOCATION = "wizard-hat";

  @Test
  void getMinioUrl() {
    final VideoServiceExtended videoServiceExtended = new VideoServiceExtended(null,null );
    videoServiceExtended.setMinioUrl(MINIO_URL);
    assertThat(videoServiceExtended.getMinioUrl()).isEqualTo(MINIO_URL);
  }

  @Test
  void getAccessKey() {
    final VideoServiceExtended videoServiceExtended = new VideoServiceExtended(null,null);
    videoServiceExtended.setAccessKey(ACCESS_KEY);
    assertThat(videoServiceExtended.getAccessKey()).isEqualTo(ACCESS_KEY);
  }

  @Test
  void getSecretKey() {
    final VideoServiceExtended videoServiceExtended = new VideoServiceExtended(null,null);
    videoServiceExtended.setSecretKey(SECRET_KEY);
    assertThat(videoServiceExtended.getSecretKey()).isEqualTo(SECRET_KEY);
  }

  @Test
  void getRootLocation() {
    final VideoServiceExtended videoServiceExtended = new VideoServiceExtended(null,null);
    videoServiceExtended.setSecretKey(ROOT_LOCATION);
    assertThat(videoServiceExtended.getSecretKey()).isEqualTo(ROOT_LOCATION);
  }
}