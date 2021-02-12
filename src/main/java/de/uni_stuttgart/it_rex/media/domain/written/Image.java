package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "content")
@DiscriminatorValue("3")
public class Image extends Media implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

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
        if (!(o instanceof Image)) return false;
        if (!super.equals(o)) return false;
        Image image = (Image) o;
        return Objects.equals(getWidth(), image.getWidth()) && Objects.equals(getHeight(), image.getHeight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWidth(), getHeight());
    }

    @Override
    public String toString() {
        return "Image{" +
            "width=" + width +
            ", height=" + height +
            '}';
    }
}
