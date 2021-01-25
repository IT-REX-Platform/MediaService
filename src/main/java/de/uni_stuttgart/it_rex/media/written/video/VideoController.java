package de.uni_stuttgart.it_rex.media.written.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
}
