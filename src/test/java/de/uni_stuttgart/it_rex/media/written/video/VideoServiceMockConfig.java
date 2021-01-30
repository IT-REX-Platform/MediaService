package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.written.FileValidatorService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.notNull;

@TestConfiguration
public class VideoServiceMockConfig {

  @Bean
  public VideoService videoService() {
    return new VideoService();
  }

  @Bean
  public VideoService videoService(VideoService videoService) {
    videoService = Mockito.spy(videoService);
    Mockito.when(videoService.save(notNull())).thenThrow(SQLException.class);

    return videoService;
  }

  @Bean
  public VideoStorageService videoStorageService() {
    return new VideoStorageService();
  }

  @Bean
  public FileValidatorService fileValidatorService() {
    return new FileValidatorService();
  }
}
