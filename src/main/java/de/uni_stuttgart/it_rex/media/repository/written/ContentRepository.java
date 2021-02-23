package de.uni_stuttgart.it_rex.media.repository.written;

import de.uni_stuttgart.it_rex.media.domain.written.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContentRepository extends JpaRepository<Content, UUID> {
}
