package de.uni_stuttgart.it_rex.media.written.video.dto;

import java.nio.file.Path;
import java.util.Objects;

public class FileMetaDataDTO {
  /**
   * The path where the file is stored in minio.
   */
  private Path minioBucket;

  /**
   * Getter.
   *
   * @return the path where the file is stored in minio.
   */
  public Path getMinioBucket() {
    return minioBucket;
  }

  /**
   * Setter.
   *
   * @param newMinioPath the new path where the file is stored in minio.
   */
  public void setMinioBucket(final Path newMinioPath) {
    this.minioBucket = newMinioPath;
  }

  /**
   * To String.
   *
   * @return the string representation of the dto.
   */
  @Override
  public String toString() {
    return "FileMetaData{" + "minioPath=" + minioBucket + '}';
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
    return Objects.equals(getMinioBucket(), that.getMinioBucket());
  }

  /**
   * Calculates the hash code of this instance of FileMetaData.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getMinioBucket());
  }
}
