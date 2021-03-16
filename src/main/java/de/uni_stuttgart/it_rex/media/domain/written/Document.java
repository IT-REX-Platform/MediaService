package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A Document.
 */
@Entity
@Table(name = "content")
@DiscriminatorValue("4")
public final class Document extends Resource implements Serializable {
}
