package de.uni_stuttgart.it_rex.media.repository.written;

import de.uni_stuttgart.it_rex.media.domain.written.Audio;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the Audio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AudioRepository extends JpaRepository<Audio, UUID> {
}
