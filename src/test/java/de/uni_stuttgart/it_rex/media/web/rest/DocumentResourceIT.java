package de.uni_stuttgart.it_rex.media.web.rest;

import de.uni_stuttgart.it_rex.media.MediaServiceApp;
import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.domain.written.Document;
import de.uni_stuttgart.it_rex.media.repository.written.DocumentRepository;
import de.uni_stuttgart.it_rex.media.service.DocumentService;

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
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@SpringBootTest(classes = { MediaServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class DocumentResourceIT {

//  private static final UUID DEFAULT_UUID = UUID.randomUUID();
//  private static final UUID UPDATED_UUID = UUID.randomUUID();

//  private static final String DEFAULT_TITLE = "AAAAAAAAAA";
//  private static final String UPDATED_TITLE = "BBBBBBBBBB";

//  private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
//  private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

//  private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
//  private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

//  private static final MIMETYPE DEFAULT_MIME_TYPE = MIMETYPE.AUDIO_MPEG;
//  private static final MIMETYPE UPDATED_MIME_TYPE = MIMETYPE.IMAGE_GIF;

//  @Autowired
//  private DocumentRepository documentRepository;

//  @Autowired
//  private DocumentMapper documentMapper;

//  @Autowired
//  private DocumentService documentService;

//  @Autowired
//  private EntityManager em;

//  @Autowired
//  private MockMvc restDocumentMockMvc;

//  private Document document;

//  /**
//   * Create an entity for this test.
//   *
//   * This is a static method, as tests for other entities might also need it,
//   * if they test an entity which requires the current entity.
//   */
//  public static Document createEntity(EntityManager em) {
//      Document document = new Document()
//          .uuid(DEFAULT_UUID)
//          .title(DEFAULT_TITLE)
//          .startDate(DEFAULT_START_DATE)
//          .endDate(DEFAULT_END_DATE)
//          .mimeType(DEFAULT_MIME_TYPE);
//      return document;
//  }
//  /**
//   * Create an updated entity for this test.
//   *
//   * This is a static method, as tests for other entities might also need it,
//   * if they test an entity which requires the current entity.
//   */
//  public static Document createUpdatedEntity(EntityManager em) {
//      Document document = new Document()
//          .uuid(UPDATED_UUID)
//          .title(UPDATED_TITLE)
//          .startDate(UPDATED_START_DATE)
//          .endDate(UPDATED_END_DATE)
//          .mimeType(UPDATED_MIME_TYPE);
//      return document;
//  }

//  @BeforeEach
//  public void initTest() {
//      document = createEntity(em);
//  }

//  @Test
//  @Transactional
//  public void createDocument() throws Exception {
//      int databaseSizeBeforeCreate = documentRepository.findAll().size();
//      // Create the Document
//      DocumentDTO documentDTO = documentMapper.toDto(document);
//      restDocumentMockMvc.perform(post("/api/documents").with(csrf())
//          .contentType(MediaType.APPLICATION_JSON)
//          .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
//          .andExpect(status().isCreated());

//      // Validate the Document in the database
//      List<Document> documentList = documentRepository.findAll();
//      assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
//      Document testDocument = documentList.get(documentList.size() - 1);
//      assertThat(testDocument.getUuid()).isEqualTo(DEFAULT_UUID);
//      assertThat(testDocument.getTitle()).isEqualTo(DEFAULT_TITLE);
//      assertThat(testDocument.getStartDate()).isEqualTo(DEFAULT_START_DATE);
//      assertThat(testDocument.getEndDate()).isEqualTo(DEFAULT_END_DATE);
//      assertThat(testDocument.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
//  }

//  @Test
//  @Transactional
//  public void createDocumentWithExistingId() throws Exception {
//      int databaseSizeBeforeCreate = documentRepository.findAll().size();

//      // Create the Document with an existing ID
//      document.setId(1L);
//      DocumentDTO documentDTO = documentMapper.toDto(document);

//      // An entity with an existing ID cannot be created, so this API call must fail
//      restDocumentMockMvc.perform(post("/api/documents").with(csrf())
//          .contentType(MediaType.APPLICATION_JSON)
//          .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
//          .andExpect(status().isBadRequest());

//      // Validate the Document in the database
//      List<Document> documentList = documentRepository.findAll();
//      assertThat(documentList).hasSize(databaseSizeBeforeCreate);
//  }


//  @Test
//  @Transactional
//  public void getAllDocuments() throws Exception {
//      // Initialize the database
//      documentRepository.saveAndFlush(document);

//      // Get all the documentList
//      restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
//          .andExpect(status().isOk())
//          .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//          .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
//          .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
//          .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
//          .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
//          .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
//          .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())));
//  }
//
//  @Test
//  @Transactional
//  public void getDocument() throws Exception {
//      // Initialize the database
//      documentRepository.saveAndFlush(document);

//      // Get the document
//      restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
//          .andExpect(status().isOk())
//          .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//          .andExpect(jsonPath("$.id").value(document.getId().intValue()))
//          .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
//          .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
//          .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
//          .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
//          .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()));
//  }
//  @Test
//  @Transactional
//  public void getNonExistingDocument() throws Exception {
//      // Get the document
//      restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
//          .andExpect(status().isNotFound());
//  }

//  @Test
//  @Transactional
//  public void updateDocument() throws Exception {
//      // Initialize the database
//      documentRepository.saveAndFlush(document);

//      int databaseSizeBeforeUpdate = documentRepository.findAll().size();

//      // Update the document
//      Document updatedDocument = documentRepository.findById(document.getId()).get();
//      // Disconnect from session so that the updates on updatedDocument are not directly saved in db
//      em.detach(updatedDocument);
//      updatedDocument
//          .uuid(UPDATED_UUID)
//          .title(UPDATED_TITLE)
//          .startDate(UPDATED_START_DATE)
//          .endDate(UPDATED_END_DATE)
//          .mimeType(UPDATED_MIME_TYPE);
//      DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

//      restDocumentMockMvc.perform(put("/api/documents").with(csrf())
//          .contentType(MediaType.APPLICATION_JSON)
//          .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
//          .andExpect(status().isOk());

//      // Validate the Document in the database
//      List<Document> documentList = documentRepository.findAll();
//      assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
//      Document testDocument = documentList.get(documentList.size() - 1);
//      assertThat(testDocument.getUuid()).isEqualTo(UPDATED_UUID);
//      assertThat(testDocument.getTitle()).isEqualTo(UPDATED_TITLE);
//      assertThat(testDocument.getStartDate()).isEqualTo(UPDATED_START_DATE);
//      assertThat(testDocument.getEndDate()).isEqualTo(UPDATED_END_DATE);
//      assertThat(testDocument.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
//  }

//  @Test
//  @Transactional
//  public void updateNonExistingDocument() throws Exception {
//      int databaseSizeBeforeUpdate = documentRepository.findAll().size();

//      // Create the Document
//      DocumentDTO documentDTO = documentMapper.toDto(document);

//      // If the entity doesn't have an ID, it will throw BadRequestAlertException
//      restDocumentMockMvc.perform(put("/api/documents").with(csrf())
//          .contentType(MediaType.APPLICATION_JSON)
//          .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
//          .andExpect(status().isBadRequest());

//      // Validate the Document in the database
//      List<Document> documentList = documentRepository.findAll();
//      assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
//  }

//  @Test
//  @Transactional
//  public void deleteDocument() throws Exception {
//      // Initialize the database
//      documentRepository.saveAndFlush(document);

//      int databaseSizeBeforeDelete = documentRepository.findAll().size();

//      // Delete the document
//      restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId()).with(csrf())
//          .accept(MediaType.APPLICATION_JSON))
//          .andExpect(status().isNoContent());

//      // Validate the database contains one less item
//      List<Document> documentList = documentRepository.findAll();
//      assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
//  }
}
