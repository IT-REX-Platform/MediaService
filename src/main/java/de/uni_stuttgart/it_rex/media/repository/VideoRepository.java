package de.uni_stuttgart.it_rex.media.repository;

import de.uni_stuttgart.it_rex.media.domain.Video;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Video entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}