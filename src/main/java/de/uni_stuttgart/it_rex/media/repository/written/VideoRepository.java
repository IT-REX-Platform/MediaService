package de.uni_stuttgart.it_rex.media.repository.written;

import de.uni_stuttgart.it_rex.media.domain.written.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the Video entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
}
