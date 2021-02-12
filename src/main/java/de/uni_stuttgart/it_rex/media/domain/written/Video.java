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
public class Video extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "length")
    private Integer length;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Video)) return false;
        if (!super.equals(o)) return false;
        Video video = (Video) o;
        return Objects.equals(getLength(), video.getLength()) && Objects.equals(getWidth(), video.getWidth()) && Objects.equals(getHeight(), video.getHeight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLength(), getWidth(), getHeight());
    }

    @Override
    public String toString() {
        return "Video{" +
            "length=" + length +
            ", width=" + width +
            ", height=" + height +
            '}';
    }
}
