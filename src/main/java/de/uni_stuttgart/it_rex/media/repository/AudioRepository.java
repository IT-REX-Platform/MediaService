package de.uni_stuttgart.it_rex.media.repository;

import de.uni_stuttgart.it_rex.media.domain.Audio;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Audio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {
}
