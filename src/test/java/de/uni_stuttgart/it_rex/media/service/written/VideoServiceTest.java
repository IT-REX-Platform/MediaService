package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VideoServiceTest {
  private static final String MINIO_URL = "https://bit.ly/3on6jNe";
  private static final String ACCESS_KEY = "MagicAccessKey";
  private static final String SECRET_KEY = "MagicSecretKey";
  private static final String ROOT_LOCATION = "wizard-hat";

  @Test
  void getMinioUrl() {
    final VideoService videoServiceExtended = new VideoService();
    videoServiceExtended.setMinioUrl(MINIO_URL);
    assertThat(videoServiceExtended.getMinioUrl()).isEqualTo(MINIO_URL);
  }

  @Test
  void getAccessKey() {
    final VideoService videoServiceExtended = new VideoService();
    videoServiceExtended.setAccessKey(ACCESS_KEY);
    assertThat(videoServiceExtended.getAccessKey()).isEqualTo(ACCESS_KEY);
  }

  @Test
  void getSecretKey() {
    final VideoService videoServiceExtended = new VideoService();
    videoServiceExtended.setSecretKey(SECRET_KEY);
    assertThat(videoServiceExtended.getSecretKey()).isEqualTo(SECRET_KEY);
  }

  @Test
  void getRootLocation() {
    final VideoService videoServiceExtended = new VideoService();
    videoServiceExtended.setSecretKey(ROOT_LOCATION);
    assertThat(videoServiceExtended.getSecretKey()).isEqualTo(ROOT_LOCATION);
  }
}