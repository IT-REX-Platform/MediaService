package de.uni_stuttgart.it_rex.media.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "mime_type")
    private MIMETYPE mimeType;

    @Column(name = "format")
    private String format;

    @Column(name = "location")
    private String location;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Media title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public Media uploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public MIMETYPE getMimeType() {
        return mimeType;
    }

    public Media mimeType(MIMETYPE mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(MIMETYPE mimeType) {
        this.mimeType = mimeType;
    }

    public String getFormat() {
        return format;
    }

    public Media format(String format) {
        this.format = format;
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLocation() {
        return location;
    }

    public Media location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return id != null && id.equals(((Media) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Media{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", format='" + getFormat() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
