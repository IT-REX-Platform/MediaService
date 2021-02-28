package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.domain.written.Content;
import de.uni_stuttgart.it_rex.media.repository.written.ContentRepository;
import de.uni_stuttgart.it_rex.media.service.mapper.ContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContentService {

  /**
   * Logger.
   */
  private static final Logger LOGGER
      = LoggerFactory.getLogger(ContentService.class);

  /**
   * The content mapper.
   */
  private ContentMapper contentMapper;

  /**
   * The course repository.
   */
  private ContentRepository contentRepository;

  /**
   * Constructor.
   *
   * @param newContentMapper     the content mapper
   * @param newContentRepository the content repository
   */
  @Autowired
  public ContentService(final ContentMapper newContentMapper,
                        final ContentRepository newContentRepository) {
    this.contentMapper = newContentMapper;
    this.contentRepository = newContentRepository;
  }

  /**
   * Update a patch without overwriting it.
   *
   * @param patch the entity to use to update a created entity.
   * @return the persisted entity.
   */
  @Transactional
  public Content patch(final Content patch) {
    LOGGER.debug("Request to update Content : {}", patch);
   final Optional<Content> contentOptional =
       contentRepository.findById(patch.getId());

    if (contentOptional.isPresent()) {
      final Content content = contentOptional.get();
      contentMapper.updateContentFromContent(patch, content);
      return contentRepository.save(content);
    }
    return null;
  }
}
