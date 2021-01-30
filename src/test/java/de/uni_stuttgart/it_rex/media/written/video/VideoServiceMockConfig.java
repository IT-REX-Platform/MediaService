package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;
import de.uni_stuttgart.it_rex.media.written.FileValidatorService;
import de.uni_stuttgart.it_rex.media.written.StorageException;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.any;

@TestConfiguration
public class VideoServiceMockConfig {

  @Bean
  @Primary
  public VideoService videoService() {
    VideoService videoService = Mockito.mock(VideoService.class);
    Mockito.when(videoService.save(any(VideoDTO.class))).thenCallRealMethod().
        thenThrow(StorageException.class);
    return videoService;
  }

  @Bean
  @Primary
  public VideoStorageService videoStorageService() {
    return new VideoStorageService();
  }

  @Bean
  @Primary
  public FileValidatorService fileValidatorService() {
    return new FileValidatorService();
  }
}
