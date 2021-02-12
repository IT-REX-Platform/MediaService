package de.uni_stuttgart.it_rex.media.web.rest;

import de.uni_stuttgart.it_rex.media.MediaServiceApp;
import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.written.Image;
import de.uni_stuttgart.it_rex.media.repository.written.ImageRepository;
import de.uni_stuttgart.it_rex.media.service.ImageService;

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
 * Integration tests for the {@link ImageResource} REST controller.
 */
@SpringBootTest(classes = { MediaServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ImageResourceIT {

// private static final UUID DEFAULT_UUID = UUID.randomUUID();
// private static final UUID UPDATED_UUID = UUID.randomUUID();

// private static final String DEFAULT_TITLE = "AAAAAAAAAA";
// private static final String UPDATED_TITLE = "BBBBBBBBBB";

// private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
// private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

// private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
// private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

// private static final MIMETYPE DEFAULT_MIME_TYPE = MIMETYPE.AUDIO_MPEG;
// private static final MIMETYPE UPDATED_MIME_TYPE = MIMETYPE.IMAGE_GIF;

// private static final Integer DEFAULT_WIDTH = 1;
// private static final Integer UPDATED_WIDTH = 2;

// private static final Integer DEFAULT_HEIGHT = 1;
// private static final Integer UPDATED_HEIGHT = 2;

// @Autowired
// private ImageRepository imageRepository;

// @Autowired
// private ImageMapper imageMapper;

// @Autowired
// private ImageService imageService;

// @Autowired
// private EntityManager em;

// @Autowired
// private MockMvc restImageMockMvc;

// private Image image;

// /**
//  * Create an entity for this test.
//  *
//  * This is a static method, as tests for other entities might also need it,
//  * if they test an entity which requires the current entity.
//  */
// public static Image createEntity(EntityManager em) {
//     Image image = new Image()
//         .uuid(DEFAULT_UUID)
//         .title(DEFAULT_TITLE)
//         .startDate(DEFAULT_START_DATE)
//         .endDate(DEFAULT_END_DATE)
//         .mimeType(DEFAULT_MIME_TYPE)
//         .width(DEFAULT_WIDTH)
//         .height(DEFAULT_HEIGHT);
//     return image;
// }
// /**
//  * Create an updated entity for this test.
//  *
//  * This is a static method, as tests for other entities might also need it,
//  * if they test an entity which requires the current entity.
//  */
// public static Image createUpdatedEntity(EntityManager em) {
//     Image image = new Image()
//         .uuid(UPDATED_UUID)
//         .title(UPDATED_TITLE)
//         .startDate(UPDATED_START_DATE)
//         .endDate(UPDATED_END_DATE)
//         .mimeType(UPDATED_MIME_TYPE)
//         .width(UPDATED_WIDTH)
//         .height(UPDATED_HEIGHT);
//     return image;
// }

// @BeforeEach
// public void initTest() {
//     image = createEntity(em);
// }

// @Test
// @Transactional
// public void createImage() throws Exception {
//     int databaseSizeBeforeCreate = imageRepository.findAll().size();
//     // Create the Image
//     ImageDTO imageDTO = imageMapper.toDto(image);
//     restImageMockMvc.perform(post("/api/images").with(csrf())
//         .contentType(MediaType.APPLICATION_JSON)
//         .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
//         .andExpect(status().isCreated());

//     // Validate the Image in the database
//     List<Image> imageList = imageRepository.findAll();
//     assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
//     Image testImage = imageList.get(imageList.size() - 1);
//     assertThat(testImage.getUuid()).isEqualTo(DEFAULT_UUID);
//     assertThat(testImage.getTitle()).isEqualTo(DEFAULT_TITLE);
//     assertThat(testImage.getStartDate()).isEqualTo(DEFAULT_START_DATE);
//     assertThat(testImage.getEndDate()).isEqualTo(DEFAULT_END_DATE);
//     assertThat(testImage.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
//     assertThat(testImage.getWidth()).isEqualTo(DEFAULT_WIDTH);
//     assertThat(testImage.getHeight()).isEqualTo(DEFAULT_HEIGHT);
// }

// @Test
// @Transactional
// public void createImageWithExistingId() throws Exception {
//     int databaseSizeBeforeCreate = imageRepository.findAll().size();

//     // Create the Image with an existing ID
//     image.setId(1L);
//     ImageDTO imageDTO = imageMapper.toDto(image);

//     // An entity with an existing ID cannot be created, so this API call must fail
//     restImageMockMvc.perform(post("/api/images").with(csrf())
//         .contentType(MediaType.APPLICATION_JSON)
//         .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
//         .andExpect(status().isBadRequest());

//     // Validate the Image in the database
//     List<Image> imageList = imageRepository.findAll();
//     assertThat(imageList).hasSize(databaseSizeBeforeCreate);
// }


// @Test
// @Transactional
// public void getAllImages() throws Exception {
//     // Initialize the database
//     imageRepository.saveAndFlush(image);

//     // Get all the imageList
//     restImageMockMvc.perform(get("/api/images?sort=id,desc"))
//         .andExpect(status().isOk())
//         .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//         .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
//         .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
//         .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
//         .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
//         .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
//         .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
//         .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
//         .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)));
// }
//
// @Test
// @Transactional
// public void getImage() throws Exception {
//     // Initialize the database
//     imageRepository.saveAndFlush(image);

//     // Get the image
//     restImageMockMvc.perform(get("/api/images/{id}", image.getId()))
//         .andExpect(status().isOk())
//         .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//         .andExpect(jsonPath("$.id").value(image.getId().intValue()))
//         .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
//         .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
//         .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
//         .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
//         .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
//         .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
//         .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT));
// }
// @Test
// @Transactional
// public void getNonExistingImage() throws Exception {
//     // Get the image
//     restImageMockMvc.perform(get("/api/images/{id}", Long.MAX_VALUE))
//         .andExpect(status().isNotFound());
// }

// @Test
// @Transactional
// public void updateImage() throws Exception {
//     // Initialize the database
//     imageRepository.saveAndFlush(image);

//     int databaseSizeBeforeUpdate = imageRepository.findAll().size();

//     // Update the image
//     Image updatedImage = imageRepository.findById(image.getId()).get();
//     // Disconnect from session so that the updates on updatedImage are not directly saved in db
//     em.detach(updatedImage);
//     updatedImage
//         .uuid(UPDATED_UUID)
//         .title(UPDATED_TITLE)
//         .startDate(UPDATED_START_DATE)
//         .endDate(UPDATED_END_DATE)
//         .mimeType(UPDATED_MIME_TYPE)
//         .width(UPDATED_WIDTH)
//         .height(UPDATED_HEIGHT);
//     ImageDTO imageDTO = imageMapper.toDto(updatedImage);

//     restImageMockMvc.perform(put("/api/images").with(csrf())
//         .contentType(MediaType.APPLICATION_JSON)
//         .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
//         .andExpect(status().isOk());

//     // Validate the Image in the database
//     List<Image> imageList = imageRepository.findAll();
//     assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
//     Image testImage = imageList.get(imageList.size() - 1);
//     assertThat(testImage.getUuid()).isEqualTo(UPDATED_UUID);
//     assertThat(testImage.getTitle()).isEqualTo(UPDATED_TITLE);
//     assertThat(testImage.getStartDate()).isEqualTo(UPDATED_START_DATE);
//     assertThat(testImage.getEndDate()).isEqualTo(UPDATED_END_DATE);
//     assertThat(testImage.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
//     assertThat(testImage.getWidth()).isEqualTo(UPDATED_WIDTH);
//     assertThat(testImage.getHeight()).isEqualTo(UPDATED_HEIGHT);
// }

// @Test
// @Transactional
// public void updateNonExistingImage() throws Exception {
//     int databaseSizeBeforeUpdate = imageRepository.findAll().size();

//     // Create the Image
//     ImageDTO imageDTO = imageMapper.toDto(image);

//     // If the entity doesn't have an ID, it will throw BadRequestAlertException
//     restImageMockMvc.perform(put("/api/images").with(csrf())
//         .contentType(MediaType.APPLICATION_JSON)
//         .content(TestUtil.convertObjectToJsonBytes(imageDTO)))
//         .andExpect(status().isBadRequest());

//     // Validate the Image in the database
//     List<Image> imageList = imageRepository.findAll();
//     assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
// }

// @Test
// @Transactional
// public void deleteImage() throws Exception {
//     // Initialize the database
//     imageRepository.saveAndFlush(image);

//     int databaseSizeBeforeDelete = imageRepository.findAll().size();

//     // Delete the image
//     restImageMockMvc.perform(delete("/api/images/{id}", image.getId()).with(csrf())
//         .accept(MediaType.APPLICATION_JSON))
//         .andExpect(status().isNoContent());

//     // Validate the database contains one less item
//     List<Image> imageList = imageRepository.findAll();
//     assertThat(imageList).hasSize(databaseSizeBeforeDelete - 1);
// }
}
