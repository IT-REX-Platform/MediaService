package de.uni_stuttgart.it_rex.media.written.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api")
public class VideoController {

    /**
     * Service for storing videos.
     */
    private final VideoStorageService videoStorageService;

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(VideoController.class);

    /**
     * Constructor.
     *
     * @param vss The service for storing videos.
     */
    @Autowired
    public VideoController(final VideoStorageService vss) {
        this.videoStorageService = vss;
    }

    /**
     * Upload a video to the system.
     * The file ist stored in a file storage
     * while metadata is persisted in an RDBMS.
     *
     * @param file               The video file to upload.
     * @param redirectAttributes The redirect attributes.
     * @return test message
     */
    @PostMapping("/videos/upload")
    public String uploadVideo(@RequestParam("file") final MultipartFile file,
                              final RedirectAttributes redirectAttributes) {

        videoStorageService.store(file);
        redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @GetMapping("/videos/download/{filename:.+}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable final String filename, @RequestHeader
        HttpHeaders headers) {

        LOGGER.info("Trying to download file " + filename);

        long chosenFileLength = videoStorageService.getLength(filename);

        HttpRange range;

        try {
            range = headers.getRange().get(0);
        } catch (IndexOutOfBoundsException e) {
            range = HttpRange.parseRanges("bytes=0-").get(0);
        }

        long start = range.getRangeStart(chosenFileLength);
        long end = range.getRangeEnd(chosenFileLength);
        long length = end - start + 1;

        LOGGER.info("Start: " + start + " End: " + end + " Length: " + length);

        Resource file = videoStorageService.loadAsResource(filename, start, length);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION,
            "inline; filename=\"" + filename + "\"");
        responseHeaders.add("Content-Type", "video/mp4");

        return ResponseEntity.ok().headers(responseHeaders).body(file);
    }
}
