package de.uni_stuttgart.it_rex.media.web.rest;

import de.uni_stuttgart.it_rex.media.service.AudioService;
import de.uni_stuttgart.it_rex.media.web.rest.errors.BadRequestAlertException;
import de.uni_stuttgart.it_rex.media.service.dto.AudioDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.uni_stuttgart.it_rex.media.domain.Audio}.
 */
@RestController
@RequestMapping("/api")
public class AudioResource {

    private final Logger log = LoggerFactory.getLogger(AudioResource.class);

    private static final String ENTITY_NAME = "mediaServiceAudio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AudioService audioService;

    public AudioResource(AudioService audioService) {
        this.audioService = audioService;
    }

    /**
     * {@code POST  /audio} : Create a new audio.
     *
     * @param audioDTO the audioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new audioDTO, or with status {@code 400 (Bad Request)} if the audio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/audio")
    public ResponseEntity<AudioDTO> createAudio(@RequestBody AudioDTO audioDTO) throws URISyntaxException {
        log.debug("REST request to save Audio : {}", audioDTO);
        if (audioDTO.getId() != null) {
            throw new BadRequestAlertException("A new audio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AudioDTO result = audioService.save(audioDTO);
        return ResponseEntity.created(new URI("/api/audio/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audio} : Updates an existing audio.
     *
     * @param audioDTO the audioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated audioDTO,
     * or with status {@code 400 (Bad Request)} if the audioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the audioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/audio")
    public ResponseEntity<AudioDTO> updateAudio(@RequestBody AudioDTO audioDTO) throws URISyntaxException {
        log.debug("REST request to update Audio : {}", audioDTO);
        if (audioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AudioDTO result = audioService.save(audioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, audioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /audio} : get all the audio.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of audio in body.
     */
    @GetMapping("/audio")
    public List<AudioDTO> getAllAudio() {
        log.debug("REST request to get all Audio");
        return audioService.findAll();
    }

    /**
     * {@code GET  /audio/:id} : get the "id" audio.
     *
     * @param id the id of the audioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the audioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/audio/{id}")
    public ResponseEntity<AudioDTO> getAudio(@PathVariable Long id) {
        log.debug("REST request to get Audio : {}", id);
        Optional<AudioDTO> audioDTO = audioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(audioDTO);
    }

    /**
     * {@code DELETE  /audio/:id} : delete the "id" audio.
     *
     * @param id the id of the audioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/audio/{id}")
    public ResponseEntity<Void> deleteAudio(@PathVariable Long id) {
        log.debug("REST request to delete Audio : {}", id);
        audioService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
