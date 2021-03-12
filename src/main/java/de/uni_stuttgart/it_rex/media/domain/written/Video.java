package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Video.
 */
@Entity
@Table(name = "content")
@DiscriminatorValue("1")
public final class Video extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Length in seconds.
     */
    @Column(name = "length")
    private Long videoLength;

    /**
     * Width in pixels.
     */
    @Column(name = "width")
    private Integer videoWidth;

    /**
     * Height in pixels.
     */
    @Column(name = "height")
    private Integer videoHeight;

    /**
     * Getter.
     *
     * @return the length.
     */
    public Long getLength() {
        return videoLength;
    }

    /**
     * Setter.
     *
     * @param newLength the length.
     */
    public void setLength(final Long newLength) {
        this.videoLength = newLength;
    }

    /**
     * Getter.
     *
     * @return the width.
     */
    public Integer getWidth() {
        return videoWidth;
    }

    /**
     * Setter.
     *
     * @param newWidth the width.
     */
    public void setWidth(final Integer newWidth) {
        this.videoWidth = newWidth;
    }

    /**
     * Getter.
     *
     * @return the height.
     */
    public Integer getHeight() {
        return videoWidth;
    }

    /**
     * Setter.
     *
     * @param newHeight the height.
     */
    public void setHeight(final Integer newHeight) {
        this.videoHeight = newHeight;
    }

    /**
     * Equals method.
     *
     * @param o the other object.
     * @return if they are equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Video)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Video video = (Video) o;
        return Objects.equals(getLength(),
            video.getLength()) && Objects.equals(getWidth(),
            video.getWidth()) && Objects.equals(getHeight(),
            video.getHeight());
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            getLength(),
            getWidth(),
            getHeight());
    }

    /**
     * To string method.
     *
     * @return this object as a string.
     */
    @Override
    public String toString() {
        return "Video{"
            + "length=" + videoLength
            + ", width=" + videoWidth
            + ", height=" + videoHeight
            + '}';
    }
}
