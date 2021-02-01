package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.repository.VideoRepository;
import de.uni_stuttgart.it_rex.media.service.VideoService;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;
import de.uni_stuttgart.it_rex.media.service.mapper.VideoMapper;
import de.uni_stuttgart.it_rex.media.written.FileValidatorService;
import de.uni_stuttgart.it_rex.media.written.StorageException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.any;

@TestConfiguration
public class VideoServiceExtendedTransactionConfig {

  @Bean
  @Primary
  @Autowired
  public VideoServiceExtended videoServiceExtended(final VideoRepository vr, final VideoMapper vm) {
    return new VideoServiceExtended(vr, vm);
  }

  @Bean
  @Primary
  public FileValidatorService fileValidatorService() {
    return new FileValidatorService();
  }
}
