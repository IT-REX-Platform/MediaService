package de.uni_stuttgart.it_rex.media.written.video.dto;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileMetaDataTest {
  private static final Path FILE_PATH_FIRST = Paths.get("videos/secret/What_really_happened_on_9_11.mp4");
  private static final Path FILE_PATH_SECOND = Paths.get("videos/secret/THE_TRUTH_ABOUT_THE_ROSSWELL_INCIDENT.webm");

  @Test
  void getMinioPath() {
    FileMetaData fileMetaData = new FileMetaData();
    fileMetaData.setMinioPath(FILE_PATH_FIRST);
    assertThat(fileMetaData.getMinioPath()).isEqualTo(FILE_PATH_FIRST);
  }

  @Test
  void equals() {
    FileMetaData fileMetaData1 = new FileMetaData();
    fileMetaData1.setMinioPath(FILE_PATH_FIRST);
    FileMetaData fileMetaData2 = new FileMetaData();
    fileMetaData2.setMinioPath(FILE_PATH_SECOND);
    FileMetaData fileMetaData3 = new FileMetaData();
    fileMetaData3.setMinioPath(FILE_PATH_FIRST);

    assertThat(fileMetaData1).isNotEqualTo(fileMetaData2);
    assertThat(fileMetaData1).isEqualTo(fileMetaData3);
    assertThat(fileMetaData1).isEqualTo(fileMetaData1);
    assertThat(fileMetaData1).isNotEqualTo(new float[]{23, 45.6f});
  }

  @Test
  void toStringTest() {
    FileMetaData fileMetaData = new FileMetaData();
    fileMetaData.setMinioPath(FILE_PATH_FIRST);
    final String expected = String.format("FileMetaData{minioPath=%s}", FILE_PATH_FIRST);
    assertThat(fileMetaData.toString()).hasToString(expected);
  }

  @Test
  void hashCodeTest() {
    FileMetaData fileMetaData1 = new FileMetaData();
    fileMetaData1.setMinioPath(FILE_PATH_FIRST);
    FileMetaData fileMetaData2 = new FileMetaData();
    fileMetaData2.setMinioPath(FILE_PATH_SECOND);

    assertThat(fileMetaData1).hasSameHashCodeAs(fileMetaData1);
    assertThat(fileMetaData1.hashCode()).isNotEqualTo(fileMetaData2.hashCode());
  }
}
