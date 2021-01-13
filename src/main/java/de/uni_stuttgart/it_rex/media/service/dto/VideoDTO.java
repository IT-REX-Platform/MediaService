package de.uni_stuttgart.it_rex.media.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link de.uni_stuttgart.it_rex.media.domain.Video} entity.
 */
public class VideoDTO implements Serializable {
    
    private Long id;

    private Integer length;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VideoDTO)) {
            return false;
        }

        return id != null && id.equals(((VideoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VideoDTO{" +
            "id=" + getId() +
            ", length=" + getLength() +
            "}";
    }
}
