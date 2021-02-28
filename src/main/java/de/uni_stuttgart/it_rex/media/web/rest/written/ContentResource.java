package de.uni_stuttgart.it_rex.media.web.rest.written;

import de.uni_stuttgart.it_rex.media.domain.written.Content;
import de.uni_stuttgart.it_rex.media.service.written.ContentService;
import de.uni_stuttgart.it_rex.media.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@RestController
@RequestMapping("/api")
public class ContentResource {

  /**
   * Logger.
   */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(ContentResource.class);

  /**
   * Service for managing contents.
   */
  private final ContentService contentService;

  /**
   * The application name.
   */
  private String applicationName;

  /**
   * Constructor.
   *
   * @param newContentService the content service
   * @param name              the name
   */
  @Autowired
  public ContentResource(
      final ContentService newContentService,
      @Value("${jhipster.clientApp.name}") final String name) {
    this.contentService = newContentService;
    this.applicationName = name;
  }

  /**
   * {@code PATCH  /content} : Patches an existing content.
   *
   * @param content the content to patch.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
   * body the updated content,
   * or with status {@code 400 (Bad Request)} if the content is not valid,
   * or with status {@code 500 (Internal Server Error)} if the content
   * couldn't be patched.
   */
  @PatchMapping("/content")
  public ResponseEntity<Content> patchContent(
      @RequestBody final Content content) {
    LOGGER.debug("REST request to patch Content : {}", content);
    if (content.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME,
          "idnull");
    }
    final Content result = contentService.patch(content);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
            true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }
}
