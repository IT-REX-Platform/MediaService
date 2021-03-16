package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A Image.
 */
@Entity
@Table(name = "content")
@DiscriminatorValue("3")
public final class Image extends Media implements Serializable {

    /**
     * Width in pixels.
     */
    @Column(name = "width")
    private Integer imageWidth;

    /**
     * Height in pixels.
     */
    @Column(name = "height")
    private Integer imageHeight;

    /**
     * Getter.
     *
     * @return the width.
     */
    public Integer getWidth() {
        return imageWidth;
    }

    /**
     * Setter.
     *
     * @param newWidth the width.
     */
    public void setWidth(final Integer newWidth) {
        this.imageWidth = newWidth;
    }

    /**
     * Getter.
     *
     * @return the height.
     */
    public Integer getHeight() {
        return imageHeight;
    }

    /**
     * Setter.
     *
     * @param newHeight the height.
     */
    public void setHeight(final Integer newHeight) {
        this.imageHeight = newHeight;
    }

    /**
     * Equals method.
     * <p>
     * The overridden method is called because the comparison is supposed to
     * only use the Id (primary key) here as Hibernate handles the rest.
     *
     * @param o the other object
     * @return if they are equal
     */
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    /**
     * Hash code.
     * <p>
     * The overridden method is called because a constant value has to be
     * returned here. This is because the Id is generated and set when the
     * entity is persisted and can be null before that.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * To string method.
     *
     * @return this object as a string.
     */
    @Override
    public String toString() {
        return "Image{"
            + "width=" + imageWidth
            + ", height=" + imageHeight
            + '}';
    }
}
