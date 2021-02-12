package de.uni_stuttgart.it_rex.media.repository.written;

import de.uni_stuttgart.it_rex.media.domain.written.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoRepositoryExtended extends JpaRepository<Video, UUID> {

  /**
   * Gets a video by its uuid.
   *
   * @param uuid the uuid
   * @return the video
   */
 // Optional<Video> findVideoByUuid(UUID uuid);
}
