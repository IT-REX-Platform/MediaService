package de.uni_stuttgart.it_rex.media.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;

/**
 * A DTO for the {@link de.uni_stuttgart.it_rex.media.domain.Media} entity.
 */
public class MediaDTO implements Serializable {
    
    private Long id;

    private String title;

    private LocalDate uploadDate;

    private MIMETYPE mimeType;

    private String format;

    private String location;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public MIMETYPE getMimeType() {
        return mimeType;
    }

    public void setMimeType(MIMETYPE mimeType) {
        this.mimeType = mimeType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaDTO)) {
            return false;
        }

        return id != null && id.equals(((MediaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", format='" + getFormat() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
