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

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "title")
  private String title;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "course_id")
  private Long courseId;

  @Column(name = "chapter_id")
  private Long chapterId;

  @Column(name = "uploader_id")
  private Long uploaderId;

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getChapterId() {
    return chapterId;
  }

  public void setChapterId(Long chapterId) {
    this.chapterId = chapterId;
  }

  public Long getUploaderId() {
    return uploaderId;
  }

  public void setUploaderId(Long uploaderId) {
    this.uploaderId = uploaderId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Content)) {
      return false;
    }
    Content content = (Content) o;
    return Objects.equals(getId(),
        content.getId())
        && Objects.equals(getTitle(), content.getTitle())
        && Objects.equals(getStartDate(), content.getStartDate())
        && Objects.equals(getEndDate(), content.getEndDate())
        && Objects.equals(getCourseId(), content.getCourseId())
        && Objects.equals(getChapterId(), content.getChapterId())
        && Objects.equals(getUploaderId(), content.getUploaderId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getStartDate(), getEndDate(), getCourseId(), getChapterId(), getUploaderId());
  }

  @Override
  public String toString() {
    return "Content{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", courseId=" + courseId +
        ", chapterId=" + chapterId +
        ", uploaderId=" + uploaderId +
        '}';
  }
}
