package de.uni_stuttgart.it_rex.media.service;

import de.uni_stuttgart.it_rex.media.domain.written.Document;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentService {

 //  private final Logger log = LoggerFactory.getLogger(DocumentService.class);

 //  private final DocumentRepository documentRepository;

 //  private final DocumentMapper documentMapper;

 //  public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
 //      this.documentRepository = documentRepository;
 //      this.documentMapper = documentMapper;
 //  }

 //  /**
 //   * Save a document.
 //   *
 //   * @param documentDTO the entity to save.
 //   * @return the persisted entity.
 //   */
 //  public DocumentDTO save(DocumentDTO documentDTO) {
 //      log.debug("Request to save Document : {}", documentDTO);
 //      Document document = documentMapper.toEntity(documentDTO);
 //      document = documentRepository.save(document);
 //      return documentMapper.toDto(document);
 //  }

 //  /**
 //   * Get all the documents.
 //   *
 //   * @return the list of entities.
 //   */
 //  @Transactional(readOnly = true)
 //  public List<DocumentDTO> findAll() {
 //      log.debug("Request to get all Documents");
 //      return documentRepository.findAll().stream()
 //          .map(documentMapper::toDto)
 //          .collect(Collectors.toCollection(LinkedList::new));
 //  }


 //  /**
 //   * Get one document by id.
 //   *
 //   * @param id the id of the entity.
 //   * @return the entity.
 //   */
 //  @Transactional(readOnly = true)
 //  public Optional<DocumentDTO> findOne(Long id) {
 //      log.debug("Request to get Document : {}", id);
 //      return documentRepository.findById(id)
 //          .map(documentMapper::toDto);
 //  }

 //  /**
 //   * Delete the document by id.
 //   *
 //   * @param id the id of the entity.
 //   */
 //  public void delete(Long id) {
 //      log.debug("Request to delete Document : {}", id);
 //      documentRepository.deleteById(id);
 //  }
}
