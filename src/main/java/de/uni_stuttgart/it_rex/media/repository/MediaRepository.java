package de.uni_stuttgart.it_rex.media.repository;

import de.uni_stuttgart.it_rex.media.domain.Media;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Media entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
