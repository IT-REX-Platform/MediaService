package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A Audio.
 */
@Entity
@Table(name = "content")
@DiscriminatorValue("2")
public final class Audio extends Media implements Serializable {

    /**
     * Length in seconds.
     */
    @Column(name = "length")
    private Long audioLength;

    /**
     * Getter.
     *
     * @return the length.
     */
    public Long getLength() {
        return audioLength;
    }

    /**
     * Setter.
     *
     * @param newLength the length.
     */
    public void setLength(final Long newLength) {
        this.audioLength = newLength;
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
        return "Audio{"
            + "length="
            + audioLength
            + '}';
    }
}
