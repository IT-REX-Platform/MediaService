package de.uni_stuttgart.it_rex.media.service.written;

import de.uni_stuttgart.it_rex.media.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.media.config.written.MinioConfig;
import de.uni_stuttgart.it_rex.media.domain.written.Video;
import de.uni_stuttgart.it_rex.media.repository.written.VideoRepository;
import de.uni_stuttgart.it_rex.media.written.testutils.MinioContainer;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;

@Disabled("Not worth implementing takes to long!")
@TestInstance(PER_CLASS)
@SpringBootTest(classes = {TestSecurityConfiguration.class, MinioConfig.class})
class VideoServiceTestIT {

  @Autowired
  private MinioContainer minioContainer;

  @Autowired
  private VideoService videoService;

  @Autowired
  private VideoRepository videoRepository;

//  @BeforeAll
//  public void setUp() {
//    try {
//      VideoService videoServiceUnwrapped =
//          ((VideoService) UnwrapProxied.unwrap(videoService));
//      videoServiceUnwrapped.setMinioUrl(minioContainer.getMinioUrl());
//      videoServiceUnwrapped.makeBucket(videoServiceUnwrapped.getRootLocation());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

  @Test
  void failDatabaseWrite()
      throws IOException,
      InvalidResponseException,
      InvalidKeyException,
      NoSuchAlgorithmException,
      ServerException,
      InternalException,
      XmlParserException,
      InsufficientDataException,
      ErrorResponseException {

    final VideoRepository videoRepositorySpy = Mockito.spy(videoRepository);
    Mockito.when(videoRepositorySpy.save(any(Video.class)))
        .thenThrow(new StorageException("Expected Error"));

    MockMultipartFile emptyFile = new MockMultipartFile(
        "FailedMetatDataWrite",
        "FailedMetatDataWrite.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hihi".getBytes()
    );
    try {
      videoService.store(emptyFile);
    } catch (StorageException e) {
      // Empty because we expect an exception to be thrown.
    }
    List<Video> dtos = videoService.findAll();
    Optional<Video> result = dtos.stream().filter(dto
        -> dto.getTitle().equals("FailedMetatDataWrite.txt")).findFirst();
    assertTrue(result.isEmpty());
  }
}
