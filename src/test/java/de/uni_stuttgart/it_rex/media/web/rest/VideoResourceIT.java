package de.uni_stuttgart.it_rex.media.web.rest;

import de.uni_stuttgart.it_rex.media.MediaServiceApp;
import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.Video;
import de.uni_stuttgart.it_rex.media.repository.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;
import de.uni_stuttgart.it_rex.media.service.mapper.VideoMapper;

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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.uni_stuttgart.it_rex.media.domain.enumeration.MIMETYPE;
/**
 * Integration tests for the {@link VideoResource} REST controller.
 */
@SpringBootTest(classes = { MediaServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class VideoResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final MIMETYPE DEFAULT_MIME_TYPE = MIMETYPE.AUDIO_MPEG;
    private static final MIMETYPE UPDATED_MIME_TYPE = MIMETYPE.IMAGE_GIF;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_LENGTH = 1;
    private static final Integer UPDATED_LENGTH = 2;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoService videoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoMockMvc;

    private Video video;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createEntity(EntityManager em) {
        Video video = new Video()
            .uuid(DEFAULT_UUID)
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .mimeType(DEFAULT_MIME_TYPE)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .length(DEFAULT_LENGTH);
        return video;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createUpdatedEntity(EntityManager em) {
        Video video = new Video()
            .uuid(UPDATED_UUID)
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .mimeType(UPDATED_MIME_TYPE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .length(UPDATED_LENGTH);
        return video;
    }

    @BeforeEach
    public void initTest() {
        video = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideo() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();
        // Create the Video
        VideoDTO videoDTO = videoMapper.toDto(video);
        restVideoMockMvc.perform(post("/api/videos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoDTO)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate + 1);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testVideo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVideo.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testVideo.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testVideo.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testVideo.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testVideo.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testVideo.getLength()).isEqualTo(DEFAULT_LENGTH);
    }

    @Test
    @Transactional
    public void createVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // Create the Video with an existing ID
        video.setId(1L);
        VideoDTO videoDTO = videoMapper.toDto(video);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoMockMvc.perform(post("/api/videos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVideos() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get all the videoList
        restVideoMockMvc.perform(get("/api/videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(video.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)));
    }
    
    @Test
    @Transactional
    public void getVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(video.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH));
    }
    @Test
    @Transactional
    public void getNonExistingVideo() throws Exception {
        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video
        Video updatedVideo = videoRepository.findById(video.getId()).get();
        // Disconnect from session so that the updates on updatedVideo are not directly saved in db
        em.detach(updatedVideo);
        updatedVideo
            .uuid(UPDATED_UUID)
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .mimeType(UPDATED_MIME_TYPE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .length(UPDATED_LENGTH);
        VideoDTO videoDTO = videoMapper.toDto(updatedVideo);

        restVideoMockMvc.perform(put("/api/videos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoDTO)))
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideo.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testVideo.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testVideo.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testVideo.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testVideo.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testVideo.getLength()).isEqualTo(UPDATED_LENGTH);
    }

    @Test
    @Transactional
    public void updateNonExistingVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Create the Video
        VideoDTO videoDTO = videoMapper.toDto(video);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoMockMvc.perform(put("/api/videos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        int databaseSizeBeforeDelete = videoRepository.findAll().size();

        // Delete the video
        restVideoMockMvc.perform(delete("/api/videos/{id}", video.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
