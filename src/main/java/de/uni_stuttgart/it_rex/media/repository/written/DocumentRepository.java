package de.uni_stuttgart.it_rex.media.repository.written;

import de.uni_stuttgart.it_rex.media.domain.written.Document;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
