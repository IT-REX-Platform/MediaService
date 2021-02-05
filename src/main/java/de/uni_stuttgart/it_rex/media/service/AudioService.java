package de.uni_stuttgart.it_rex.media.service;

import de.uni_stuttgart.it_rex.media.domain.Audio;
import de.uni_stuttgart.it_rex.media.repository.AudioRepository;
import de.uni_stuttgart.it_rex.media.service.dto.AudioDTO;
import de.uni_stuttgart.it_rex.media.service.mapper.AudioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Audio}.
 */
@Service
@Transactional
public class AudioService {

    private final Logger log = LoggerFactory.getLogger(AudioService.class);

    private final AudioRepository audioRepository;

    private final AudioMapper audioMapper;

    public AudioService(AudioRepository audioRepository, AudioMapper audioMapper) {
        this.audioRepository = audioRepository;
        this.audioMapper = audioMapper;
    }

    /**
     * Save a audio.
     *
     * @param audioDTO the entity to save.
     * @return the persisted entity.
     */
    public AudioDTO save(AudioDTO audioDTO) {
        log.debug("Request to save Audio : {}", audioDTO);
        Audio audio = audioMapper.toEntity(audioDTO);
        audio = audioRepository.save(audio);
        return audioMapper.toDto(audio);
    }

    /**
     * Get all the audio.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AudioDTO> findAll() {
        log.debug("Request to get all Audio");
        return audioRepository.findAll().stream()
            .map(audioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one audio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AudioDTO> findOne(Long id) {
        log.debug("Request to get Audio : {}", id);
        return audioRepository.findById(id)
            .map(audioMapper::toDto);
    }

    /**
     * Delete the audio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Audio : {}", id);
        audioRepository.deleteById(id);
    }
}
