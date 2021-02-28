package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "content")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    discriminatorType = DiscriminatorType.INTEGER,
    name = "dtype",
    columnDefinition = "TINYINT(1)"
)
public class Content implements Serializable {

  /**
   * Identifier.
   */
  @Id
  @GeneratedValue
  private UUID id;

  /**
   * Title of the Content Item.
   */
  @Column(name = "title")
  private String title;

  /**
   * Start date of the Content item.
   */
  @Column(name = "start_date")
  private LocalDate startDate;

  /**
   * End date of the Content item.
   */
  @Column(name = "end_date")
  private LocalDate endDate;

  /**
   * Id of the course this item belongs to.
   */
  @Column(name = "course_id")
  private UUID courseId;

  /**
   * Id of the chapter this item belongs to.
   */
  @Column(name = "chapter_id")
  private UUID chapterId;

  /**
   * Id of the uploader of this Content item.
   */
  @Column(name = "uploader_id")
  private UUID uploaderId;

  /**
   * Getter.
   *
   * @return the id.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Setter. Necessary for tests. DON't use this.
   *
   * @param newId
   */
  public void setId(final UUID newId) {
    this.id = newId;
  }

  /**
   * Getter.
   *
   * @return the title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Setter.
   *
   * @param newTitle the title.
   */
  public void setTitle(final String newTitle) {
    this.title = newTitle;
  }

  /**
   * Getter.
   *
   * @return the start date.
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Setter.
   *
   * @param newStartDate the start date.
   */
  public void setStartDate(final LocalDate newStartDate) {
    this.startDate = newStartDate;
  }

  /**
   * Getter.
   *
   * @return the end date.
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * Setter.
   *
   * @param newEndDate the end date.
   */
  public void setEndDate(final LocalDate newEndDate) {
    this.endDate = newEndDate;
  }

  /**
   * Getter.
   *
   * @return the course id.
   */
  public UUID getCourseId() {
    return courseId;
  }

  /**
   * Setter.
   *
   * @param newCourseId the course id.
   */
  public void setCourseId(final UUID newCourseId) {
    this.courseId = newCourseId;
  }

  /**
   * Getter.
   *
   * @return the chapter id.
   */
  public UUID getChapterId() {
    return chapterId;
  }

  /**
   * Setter.
   *
   * @param newChapterId the chapter id.
   */
  public void setChapterId(final UUID newChapterId) {
    this.chapterId = newChapterId;
  }

  /**
   * Getter.
   *
   * @return the uploader id
   */
  public UUID getUploaderId() {
    return uploaderId;
  }

  /**
   * Setter.
   *
   * @param newUploaderId the uploader id
   */
  public void setUploaderId(final UUID newUploaderId) {
    this.uploaderId = newUploaderId;
  }

  /**
   * Equals method.
   *
   * @param o the other object
   * @return if they are equal
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Content)) {
      return false;
    }
    Content content = (Content) o;
    return Objects.equals(getId(), content.getId())
        && Objects.equals(getTitle(), content.getTitle())
        && Objects.equals(getStartDate(), content.getStartDate())
        && Objects.equals(getEndDate(), content.getEndDate())
        && Objects.equals(getCourseId(), content.getCourseId())
        && Objects.equals(getChapterId(), content.getChapterId())
        && Objects.equals(getUploaderId(), content.getUploaderId());
  }

  /**
   * Hash code method.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        getId(),
        getTitle(),
        getStartDate(),
        getEndDate(),
        getCourseId(),
        getChapterId(),
        getUploaderId());
  }

  /**
   * To string method.
   *
   * @return this object as a string.
   */
  @Override
  public String toString() {
    return "Content{"
        + "id=" + id
        + ", title='" + title + '\''
        + ", startDate=" + startDate
        + ", endDate=" + endDate
        + ", courseId=" + courseId
        + ", chapterId=" + chapterId
        + ", uploaderId=" + uploaderId + '}';
  }
}
