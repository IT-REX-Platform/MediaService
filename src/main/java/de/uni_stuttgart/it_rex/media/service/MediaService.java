package de.uni_stuttgart.it_rex.media.service;

import de.uni_stuttgart.it_rex.media.domain.Media;
import de.uni_stuttgart.it_rex.media.repository.MediaRepository;
import de.uni_stuttgart.it_rex.media.service.dto.MediaDTO;
import de.uni_stuttgart.it_rex.media.service.mapper.MediaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Media}.
 */
@Service
@Transactional
public class MediaService {

    private final Logger log = LoggerFactory.getLogger(MediaService.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaService(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    /**
     * Save a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaDTO save(MediaDTO mediaDTO) {
        log.debug("Request to save Media : {}", mediaDTO);
        Media media = mediaMapper.toEntity(mediaDTO);
        media = mediaRepository.save(media);
        return mediaMapper.toDto(media);
    }

    /**
     * Get all the media.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediaDTO> findAll() {
        log.debug("Request to get all Media");
        return mediaRepository.findAll().stream()
            .map(mediaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one media by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MediaDTO> findOne(Long id) {
        log.debug("Request to get Media : {}", id);
        return mediaRepository.findById(id)
            .map(mediaMapper::toDto);
    }

    /**
     * Delete the media by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Media : {}", id);
        mediaRepository.deleteById(id);
    }
}
