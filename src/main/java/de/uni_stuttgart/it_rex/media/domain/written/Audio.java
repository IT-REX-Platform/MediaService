package de.uni_stuttgart.it_rex.media.domain.written;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
public class Audio extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "length")
    private Integer length;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Audio)) return false;
        if (!super.equals(o)) return false;
        Audio audio = (Audio) o;
        return Objects.equals(getLength(), audio.getLength());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLength());
    }

    @Override
    public String toString() {
        return "Audio{" +
            "length=" + length +
            '}';
    }
}
