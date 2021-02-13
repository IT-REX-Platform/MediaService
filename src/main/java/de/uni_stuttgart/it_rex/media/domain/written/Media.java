package de.uni_stuttgart.it_rex.media.domain.written;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "content")
public abstract class Media extends Resource implements Serializable {
}
