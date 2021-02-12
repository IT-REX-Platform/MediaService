package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Audio.
 */
@Entity
@Table(name = "content")
@DiscriminatorValue("2")
public final class Audio extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Length in seconds.
     */
    @Column(name = "length")
    private Integer audioLength;

    /**
     * Getter.
     *
     * @return the length.
     */
    public Integer getLength() {
        return audioLength;
    }

    /**
     * Setter.
     *
     * @param newLength the length.
     */
    public void setLength(final Integer newLength) {
        this.audioLength = newLength;
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
        if (!(o instanceof Audio)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Audio audio = (Audio) o;
        return Objects.equals(getLength(), audio.getLength());
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLength());
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
