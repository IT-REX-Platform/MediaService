package de.uni_stuttgart.it_rex.media.written.video.dto;

import java.nio.file.Path;
import java.util.Objects;

public class FileMetaDataDTO {
  /**
   * The path where the file is stored in minio.
   */
  private Path minioPath;

  /**
   * Getter.
   *
   * @return the path where the file is stored in minio.
   */
  public Path getMinioPath() {
    return minioPath;
  }

  /**
   * Setter.
   *
   * @param newMinioPath the new path where the file is stored in minio.
   */
  public void setMinioPath(final Path newMinioPath) {
    this.minioPath = newMinioPath;
  }

  /**
   * To String.
   *
   * @return the string representation of the dto.
   */
  @Override
  public String toString() {
    return "FileMetaData{" + "minioPath=" + minioPath + '}';
  }

  /**
   * Checks if two instances of FileMetaData are equal.
   *
   * @param o the other instance.
   * @return if they are equal
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FileMetaDataDTO)) {
      return false;
    }
    FileMetaDataDTO that = (FileMetaDataDTO) o;
    return Objects.equals(getMinioPath(), that.getMinioPath());
  }

  /**
   * Calculates the hash code of this instance of FileMetaData.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getMinioPath());
  }
}
