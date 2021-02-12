package de.uni_stuttgart.it_rex.media.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.UUID;
import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;

/**
 * A DTO for the {@link de.uni_stuttgart.it_rex.media.domain.Audio} entity.
 */
public class AudioDTO implements Serializable {
    
    private Long id;

    private UUID uuid;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private MIMETYPE mimeType;

    private Integer length;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public MIMETYPE getMimeType() {
        return mimeType;
    }

    public void setMimeType(MIMETYPE mimeType) {
        this.mimeType = mimeType;
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
        if (!(o instanceof AudioDTO)) {
            return false;
        }

        return id != null && id.equals(((AudioDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AudioDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", length=" + getLength() +
            "}";
    }
}