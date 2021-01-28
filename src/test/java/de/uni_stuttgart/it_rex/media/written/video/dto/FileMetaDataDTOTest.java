package de.uni_stuttgart.it_rex.media.written.video.dto;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileMetaDataDTOTest {
  private static String NAME_FIRST = "What_really_happened_on_9_11.mp4";
  private static String NAME_SECOND = "THE_TRUTH_ABOUT_THE_ROSSWELL_INCIDENT.webm";
  private static final Path BUCKET_FIRST = Paths.get("videos/secret/");
  private static final Path BUCKET_SECOND = Paths.get("videos/public/");

  @Test
  void getMinioBucket() {
    FileMetaDataDTO fileMetaDataDTO = new FileMetaDataDTO();
    fileMetaDataDTO.setMinioBucket(BUCKET_FIRST);
    assertThat(fileMetaDataDTO.getMinioBucket()).isEqualTo(BUCKET_FIRST);
  }

  @Test
  void getName() {
    FileMetaDataDTO fileMetaDataDTO = new FileMetaDataDTO();
    fileMetaDataDTO.setName(NAME_FIRST);
    assertThat(fileMetaDataDTO.getName()).isEqualTo(NAME_FIRST);
  }

  @Test
  void equals() {
    FileMetaDataDTO fileMetaDataDTO1 = new FileMetaDataDTO();
    fileMetaDataDTO1.setMinioBucket(BUCKET_FIRST);
    FileMetaDataDTO fileMetaDataDTO2 = new FileMetaDataDTO();
    fileMetaDataDTO2.setMinioBucket(BUCKET_SECOND);
    FileMetaDataDTO fileMetaDataDTO3 = new FileMetaDataDTO();
    fileMetaDataDTO3.setMinioBucket(BUCKET_FIRST);

    assertThat(fileMetaDataDTO1).isNotEqualTo(fileMetaDataDTO2);
    assertThat(fileMetaDataDTO1).isEqualTo(fileMetaDataDTO3);
    assertThat(fileMetaDataDTO1).isEqualTo(fileMetaDataDTO1);
    assertThat(fileMetaDataDTO1).isNotEqualTo(new float[]{23, 45.6f});
  }

  @Test
  void toStringTest() {
    FileMetaDataDTO fileMetaDataDTO = new FileMetaDataDTO();
    fileMetaDataDTO.setMinioBucket(BUCKET_FIRST);
    final String expected = String.format("FileMetaData{minioPath=%s}", BUCKET_FIRST);
    assertThat(fileMetaDataDTO.toString()).hasToString(expected);
  }

  @Test
  void hashCodeTest() {
    FileMetaDataDTO fileMetaDataDTO1 = new FileMetaDataDTO();
    fileMetaDataDTO1.setMinioBucket(BUCKET_FIRST);
    FileMetaDataDTO fileMetaDataDTO2 = new FileMetaDataDTO();
    fileMetaDataDTO2.setMinioBucket(BUCKET_SECOND);

    assertThat(fileMetaDataDTO1).hasSameHashCodeAs(fileMetaDataDTO1);
    assertThat(fileMetaDataDTO1.hashCode()).isNotEqualTo(fileMetaDataDTO2.hashCode());
  }
}
