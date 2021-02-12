package de.uni_stuttgart.it_rex.media.config.written;

import de.uni_stuttgart.it_rex.media.repository.written.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import de.uni_stuttgart.it_rex.media.service.written.FileValidatorService;
import de.uni_stuttgart.it_rex.media.written.testutils.MinioContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.nio.file.Path;

@Import(MockitoSkipAutowireConfiguration.class)
@TestConfiguration
public class MinioConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public MinioContainer minioContainer() {
    return new MinioContainer();
  }

  @Bean
  @Primary
  @Autowired
  public VideoService videoService(final VideoRepository videoRepository,
                                   final FileValidatorService fileValidatorService,
                                   final ApplicationEventPublisher applicationEventPublisher,
                                   final MinioContainer minioContainer,
                                   @Value("${minio.access-key}") final String accessKey,
                                   @Value("${minio.secret-key}") final String secretKey,
                                   @Value("${minio.root-location}") final Path newLocation) {
    VideoService videoService = new VideoService();
    videoService.setVideoRepository(videoRepository);
    videoService.setFileValidatorService(fileValidatorService);
    videoService.setApplicationEventPublisher(applicationEventPublisher);
    videoService.setAccessKey(accessKey);
    videoService.setSecretKey(secretKey);
    videoService.setRootLocation(newLocation);
    videoService.setMinioUrl(minioContainer.getMinioUrl());

    return videoService;
  }

  @Bean
  @Primary
  public FileValidatorService fileValidatorService() {
    return new FileValidatorService();
  }
}
