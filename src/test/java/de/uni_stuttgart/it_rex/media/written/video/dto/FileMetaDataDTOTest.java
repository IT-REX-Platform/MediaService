package de.uni_stuttgart.it_rex.media.written.video.dto;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileMetaDataDTOTest {
  private static final Path FILE_PATH_FIRST = Paths.get("videos/secret/What_really_happened_on_9_11.mp4");
  private static final Path FILE_PATH_SECOND = Paths.get("videos/secret/THE_TRUTH_ABOUT_THE_ROSSWELL_INCIDENT.webm");

  @Test
  void getMinioPath() {
    FileMetaDataDTO fileMetaDataDTO = new FileMetaDataDTO();
    fileMetaDataDTO.setMinioPath(FILE_PATH_FIRST);
    assertThat(fileMetaDataDTO.getMinioPath()).isEqualTo(FILE_PATH_FIRST);
  }

  @Test
  void equals() {
    FileMetaDataDTO fileMetaDataDTO1 = new FileMetaDataDTO();
    fileMetaDataDTO1.setMinioPath(FILE_PATH_FIRST);
    FileMetaDataDTO fileMetaDataDTO2 = new FileMetaDataDTO();
    fileMetaDataDTO2.setMinioPath(FILE_PATH_SECOND);
    FileMetaDataDTO fileMetaDataDTO3 = new FileMetaDataDTO();
    fileMetaDataDTO3.setMinioPath(FILE_PATH_FIRST);

    assertThat(fileMetaDataDTO1).isNotEqualTo(fileMetaDataDTO2);
    assertThat(fileMetaDataDTO1).isEqualTo(fileMetaDataDTO3);
    assertThat(fileMetaDataDTO1).isEqualTo(fileMetaDataDTO1);
    assertThat(fileMetaDataDTO1).isNotEqualTo(new float[]{23, 45.6f});
  }

  @Test
  void toStringTest() {
    FileMetaDataDTO fileMetaDataDTO = new FileMetaDataDTO();
    fileMetaDataDTO.setMinioPath(FILE_PATH_FIRST);
    final String expected = String.format("FileMetaData{minioPath=%s}", FILE_PATH_FIRST);
    assertThat(fileMetaDataDTO.toString()).hasToString(expected);
  }

  @Test
  void hashCodeTest() {
    FileMetaDataDTO fileMetaDataDTO1 = new FileMetaDataDTO();
    fileMetaDataDTO1.setMinioPath(FILE_PATH_FIRST);
    FileMetaDataDTO fileMetaDataDTO2 = new FileMetaDataDTO();
    fileMetaDataDTO2.setMinioPath(FILE_PATH_SECOND);

    assertThat(fileMetaDataDTO1).hasSameHashCodeAs(fileMetaDataDTO1);
    assertThat(fileMetaDataDTO1.hashCode()).isNotEqualTo(fileMetaDataDTO2.hashCode());
  }
}
