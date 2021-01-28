package de.uni_stuttgart.it_rex.media.written.video.dto;

import java.nio.file.Path;
import java.util.Objects;

public class FileMetaDataDTO {
  /**
   * The path where the file is stored in minio.
   */
  private Path minioBucket;

  /**
   * the name of the file.
   */
  private String name;

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
   * Getter.
   *
   * @return the name of the file.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter.
   *
   * @param newName the name of the file.
   */
  public void setName(final String newName) {
    this.name = newName;
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
    return Objects.equals(getMinioBucket(), that.getMinioBucket())
        && Objects.equals(getName(), that.getName());
  }

  /**
   * Calculates the hash code of this instance of FileMetaData.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getMinioBucket(), getName());
  }
}






