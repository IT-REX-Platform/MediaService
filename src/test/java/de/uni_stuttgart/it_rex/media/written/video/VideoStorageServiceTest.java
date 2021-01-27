package de.uni_stuttgart.it_rex.media.written.video;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VideoStorageServiceTest {
  private static final String MINIO_URL = "https://bit.ly/3on6jNe";
  private static final String ACCESS_KEY = "MagicAccessKey";
  private static final String SECRET_KEY = "MagicSecretKey";
  private static final String ROOT_LOCATION = "wizard-hat";

  @Test
  void getMinioUrl() {
    final VideoStorageService videoStorageService = new VideoStorageService();
    videoStorageService.setMinioUrl(MINIO_URL);
    assertThat(videoStorageService.getMinioUrl()).isEqualTo(MINIO_URL);
  }

  @Test
  void getAccessKey() {
    final VideoStorageService videoStorageService = new VideoStorageService();
    videoStorageService.setAccessKey(ACCESS_KEY);
    assertThat(videoStorageService.getAccessKey()).isEqualTo(ACCESS_KEY);
  }

  @Test
  void getSecretKey() {
    final VideoStorageService videoStorageService = new VideoStorageService();
    videoStorageService.setSecretKey(SECRET_KEY);
    assertThat(videoStorageService.getSecretKey()).isEqualTo(SECRET_KEY);
  }

  @Test
  void getRootLocation() {
    final VideoStorageService videoStorageService = new VideoStorageService();
    videoStorageService.setSecretKey(ROOT_LOCATION);
    assertThat(videoStorageService.getSecretKey()).isEqualTo(ROOT_LOCATION);
  }
}