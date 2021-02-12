package de.uni_stuttgart.it_rex.media.domain.written;

import de.uni_stuttgart.it_rex.media.domain.written.enumeration.MIMETYPE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "content")
public class Resource extends Content {

  /**
   * Mimetype of this Resource.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "mime_type")
  private MIMETYPE mimeType;

  public MIMETYPE getMimeType() {
    return mimeType;
  }

  public void setMimeType(final MIMETYPE mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Equals method.
   *
   * @param o the other object.
   * @return if they are equal.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Resource)) {
      return false;
    }
    if (!super.equals(o)) return false;
    Resource resource = (Resource) o;
    return getMimeType() == resource.getMimeType();
  }

  /**
   * Hash code.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getMimeType());
  }


  /**
   * To string method.
   *
   * @return this object as a string.
   */
  @Override
  public String toString() {
    return "Resource{}";
  }
}
