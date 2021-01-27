package de.uni_stuttgart.it_rex.media.written;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileValidatorServiceTest {

  @Test
  void validateEmptyContentType() {
    FileValidatorService fileValidatorService = new FileValidatorService();
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "hello.txt",
        null,
        "Hello, World!".getBytes()
    );
    assertThrows(IllegalArgumentException.class, () -> {
      fileValidatorService.validate(file);
    });
  }

  @Test
  void validateCorrectFile() {
    FileValidatorService fileValidatorService = new FileValidatorService();
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );
    assertThat(fileValidatorService.validate(file)).isTrue();
  }
}