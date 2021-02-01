package de.uni_stuttgart.it_rex.media.written;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileValidatorService {

  /**
   * Validates if the file is acceptable.
   *
   * @param file the file to validated
   * @return if it is acceptable
   */
  public boolean validate(final MultipartFile file) {
    Assert.notNull(file.getContentType(), "The Content Type cannot be null!");
    if (file.isEmpty()) {
      throw new StorageException("Failed to store empty file!");
    }
    return true;
  }
}
