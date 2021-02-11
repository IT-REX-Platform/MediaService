package de.uni_stuttgart.it_rex.media.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;

/**
 * A Audio.
 */
@Entity
@Table(name = "audio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Audio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "mime_type")
    private MIMETYPE mimeType;

    @Column(name = "length")
    private Integer length;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Audio uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public Audio title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Audio startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Audio endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public MIMETYPE getMimeType() {
        return mimeType;
    }

    public Audio mimeType(MIMETYPE mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(MIMETYPE mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getLength() {
        return length;
    }

    public Audio length(Integer length) {
        this.length = length;
        return this;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Audio)) {
            return false;
        }
        return id != null && id.equals(((Audio) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Audio{" +
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
