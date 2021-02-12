package de.uni_stuttgart.it_rex.media.web.rest;

import de.uni_stuttgart.it_rex.media.domain.written.Video;

import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link Video}.
 */
@RestController
@RequestMapping("/api")
public class VideoResource {

//   private final Logger log = LoggerFactory.getLogger(VideoResource.class);

//   private static final String ENTITY_NAME = "mediaServiceVideo";

//   @Value("${jhipster.clientApp.name}")
//   private String applicationName;

//   private final VideoService videoService;

//   public VideoResource(VideoService videoService) {
//       this.videoService = videoService;
//   }

//   /**
//    * {@code POST  /videos} : Create a new video.
//    *
//    * @param videoDTO the videoDTO to create.
//    * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videoDTO, or with status {@code 400 (Bad Request)} if the video has already an ID.
//    * @throws URISyntaxException if the Location URI syntax is incorrect.
//    */
//   @PostMapping("/videos")
//   public ResponseEntity<VideoDTO> createVideo(@RequestBody VideoDTO videoDTO) throws URISyntaxException {
//       log.debug("REST request to save Video : {}", videoDTO);
//       if (videoDTO.getId() != null) {
//           throw new BadRequestAlertException("A new video cannot already have an ID", ENTITY_NAME, "idexists");
//       }
//       VideoDTO result = videoService.save(videoDTO);
//       return ResponseEntity.created(new URI("/api/videos/" + result.getId()))
//           .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//           .body(result);
//   }

//   /**
//    * {@code PUT  /videos} : Updates an existing video.
//    *
//    * @param videoDTO the videoDTO to update.
//    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoDTO,
//    * or with status {@code 400 (Bad Request)} if the videoDTO is not valid,
//    * or with status {@code 500 (Internal Server Error)} if the videoDTO couldn't be updated.
//    * @throws URISyntaxException if the Location URI syntax is incorrect.
//    */
//   @PutMapping("/videos")
//   public ResponseEntity<VideoDTO> updateVideo(@RequestBody VideoDTO videoDTO) throws URISyntaxException {
//       log.debug("REST request to update Video : {}", videoDTO);
//       if (videoDTO.getId() == null) {
//           throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//       }
//       VideoDTO result = videoService.save(videoDTO);
//       return ResponseEntity.ok()
//           .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videoDTO.getId().toString()))
//           .body(result);
//   }

//   /**
//    * {@code GET  /videos} : get all the videos.
//    *
//    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videos in body.
//    */
//   @GetMapping("/videos")
//   public List<VideoDTO> getAllVideos() {
//       log.debug("REST request to get all Videos");
//       return videoService.findAll();
//   }

//   /**
//    * {@code GET  /videos/:id} : get the "id" video.
//    *
//    * @param id the id of the videoDTO to retrieve.
//    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoDTO, or with status {@code 404 (Not Found)}.
//    */
//   @GetMapping("/videos/{id}")
//   public ResponseEntity<VideoDTO> getVideo(@PathVariable Long id) {
//       log.debug("REST request to get Video : {}", id);
//       Optional<VideoDTO> videoDTO = videoService.findOne(id);
//       return ResponseUtil.wrapOrNotFound(videoDTO);
//   }

//   /**
//    * {@code DELETE  /videos/:id} : delete the "id" video.
//    *
//    * @param id the id of the videoDTO to delete.
//    * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//    */
//   @DeleteMapping("/videos/{id}")
//   public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
//       log.debug("REST request to delete Video : {}", id);
//       videoService.delete(id);
//       return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
//   }
}
