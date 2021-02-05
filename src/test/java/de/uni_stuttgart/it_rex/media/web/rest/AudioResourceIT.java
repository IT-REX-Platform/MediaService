package de.uni_stuttgart.it_rex.media.web.rest;

import de.uni_stuttgart.it_rex.media.MediaServiceApp;
import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.Audio;
import de.uni_stuttgart.it_rex.media.repository.AudioRepository;
import de.uni_stuttgart.it_rex.media.service.AudioService;
import de.uni_stuttgart.it_rex.media.service.dto.AudioDTO;
import de.uni_stuttgart.it_rex.media.service.mapper.AudioMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;
/**
 * Integration tests for the {@link AudioResource} REST controller.
 */
@SpringBootTest(classes = { MediaServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AudioResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final MIMETYPE DEFAULT_MIME_TYPE = MIMETYPE.AUDIO_MPEG;
    private static final MIMETYPE UPDATED_MIME_TYPE = MIMETYPE.IMAGE_GIF;

    private static final Integer DEFAULT_LENGTH = 1;
    private static final Integer UPDATED_LENGTH = 2;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private AudioMapper audioMapper;

    @Autowired
    private AudioService audioService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAudioMockMvc;

    private Audio audio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audio createEntity(EntityManager em) {
        Audio audio = new Audio()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .mimeType(DEFAULT_MIME_TYPE)
            .length(DEFAULT_LENGTH);
        return audio;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audio createUpdatedEntity(EntityManager em) {
        Audio audio = new Audio()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .mimeType(UPDATED_MIME_TYPE)
            .length(UPDATED_LENGTH);
        return audio;
    }

    @BeforeEach
    public void initTest() {
        audio = createEntity(em);
    }

    @Test
    @Transactional
    public void createAudio() throws Exception {
        int databaseSizeBeforeCreate = audioRepository.findAll().size();
        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);
        restAudioMockMvc.perform(post("/api/audio").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isCreated());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeCreate + 1);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAudio.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAudio.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAudio.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testAudio.getLength()).isEqualTo(DEFAULT_LENGTH);
    }

    @Test
    @Transactional
    public void createAudioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = audioRepository.findAll().size();

        // Create the Audio with an existing ID
        audio.setId(1L);
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAudioMockMvc.perform(post("/api/audio").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get all the audioList
        restAudioMockMvc.perform(get("/api/audio?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audio.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)));
    }
    
    @Test
    @Transactional
    public void getAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        // Get the audio
        restAudioMockMvc.perform(get("/api/audio/{id}", audio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(audio.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH));
    }
    @Test
    @Transactional
    public void getNonExistingAudio() throws Exception {
        // Get the audio
        restAudioMockMvc.perform(get("/api/audio/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Update the audio
        Audio updatedAudio = audioRepository.findById(audio.getId()).get();
        // Disconnect from session so that the updates on updatedAudio are not directly saved in db
        em.detach(updatedAudio);
        updatedAudio
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .mimeType(UPDATED_MIME_TYPE)
            .length(UPDATED_LENGTH);
        AudioDTO audioDTO = audioMapper.toDto(updatedAudio);

        restAudioMockMvc.perform(put("/api/audio").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isOk());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
        Audio testAudio = audioList.get(audioList.size() - 1);
        assertThat(testAudio.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAudio.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAudio.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAudio.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testAudio.getLength()).isEqualTo(UPDATED_LENGTH);
    }

    @Test
    @Transactional
    public void updateNonExistingAudio() throws Exception {
        int databaseSizeBeforeUpdate = audioRepository.findAll().size();

        // Create the Audio
        AudioDTO audioDTO = audioMapper.toDto(audio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAudioMockMvc.perform(put("/api/audio").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Audio in the database
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAudio() throws Exception {
        // Initialize the database
        audioRepository.saveAndFlush(audio);

        int databaseSizeBeforeDelete = audioRepository.findAll().size();

        // Delete the audio
        restAudioMockMvc.perform(delete("/api/audio/{id}", audio.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Audio> audioList = audioRepository.findAll();
        assertThat(audioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
