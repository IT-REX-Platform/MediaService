package de.uni_stuttgart.it_rex.media.domain.written;

import de.uni_stuttgart.it_rex.media.domain.written.enumeration.MIMETYPE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "content")
public abstract class Resource extends Content {

    /**
     * Mimetype of this Resource.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "mime_type")
    private MIMETYPE mimeType;

    /**
     * Getter.
     *
     * @return the mime type
     */
    public MIMETYPE getMimeType() {
        return mimeType;
    }

    /**
     * Setter.
     *
     * @param newMimeType the mime type.
     */
    public void setMimeType(final MIMETYPE newMimeType) {
        this.mimeType = newMimeType;
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
        return "Resource{}";
    }
}
