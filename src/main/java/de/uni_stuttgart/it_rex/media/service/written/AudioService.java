package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.domain.written.Audio;
import de.uni_stuttgart.it_rex.media.repository.written.AudioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Audio}.
 */
@Service
@Transactional
public class AudioService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AudioService.class);

  private AudioRepository audioRepository;

  public AudioService() {
  }

  /**
   * Save a audio.
   *
   * @param audio the entity to save.
   * @return the persisted entity.
   */
  public Audio save(Audio audio) {
    LOGGER.debug("Request to save Audio : {}", audio);
    return audioRepository.save(audio);
  }

  /**
   * Get all the audio.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<Audio> findAll() {
    LOGGER.debug("Request to get all Audio");
    return audioRepository.findAll();
  }


  /**
   * Get one audio by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<Audio> findOne(UUID id) {
    LOGGER.debug("Request to get Audio : {}", id);
    return audioRepository.findById(id);
  }

  /**
   * Delete the audio by id.
   *
   * @param id the id of the entity.
   */
  public void delete(UUID id) {
    LOGGER.debug("Request to delete Audio : {}", id);
    audioRepository.deleteById(id);
  }

  /**
   * Getter.
   *
   * @return the audioRepository
   */
  public AudioRepository getAudioRepository() {
    return audioRepository;
  }

  /**
   * Setter.
   *
   * @param newAudioRepository the audioRepository
   */
  @Autowired
  public void setAudioRepository(AudioRepository newAudioRepository) {
    this.audioRepository = newAudioRepository;
  }
}
