package de.uni_stuttgart.it_rex.media.config.written;

import de.uni_stuttgart.it_rex.media.repository.written.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.mapper.written.VideoMapper;
import de.uni_stuttgart.it_rex.media.service.written.FileValidatorService;
import de.uni_stuttgart.it_rex.media.service.written.VideoService;
import de.uni_stuttgart.it_rex.media.written.testutils.MinioContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.nio.file.Path;

@TestConfiguration
public class MinioConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public MinioContainer minioContainer() {
    return new MinioContainer();
  }

  @Bean
  @Primary
  @Autowired
  @Lazy
  public VideoService videoService(MinioContainer minioContainer,
                                   ApplicationEventPublisher newApplicationEventPublisher,
                                   FileValidatorService newFileValidatorService,
                                   VideoRepository newVideoRepository,
                                   VideoMapper newVideoMapper,
                                   @Value("${minio.url}") final String newMinioUrl,
                                   @Value("${minio.access-key}") final String newAccessKey,
                                   @Value("${minio.secret-key}") final String newSecretKey,
                                   @Value("${minio.root-location}") final Path newLocation) {
    VideoService videoService = new VideoService(
        newApplicationEventPublisher,
        newFileValidatorService,
        newVideoRepository,
        newVideoMapper,
        newMinioUrl,
        newAccessKey,
        newSecretKey,
        newLocation);
    videoService.setMinioUrl(minioContainer.getMinioUrl());
    return videoService;
  }
}
