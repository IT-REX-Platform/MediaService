package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.written.StorageException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class VideoStorageService {

    /**
     * Logger.
     */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(VideoStorageService.class);

    /**
     * The url to minio
     */
    private String minioUrl;

    /**
     * The access key of the minio instance.
     */
    private String accessKey;

    /**
     * The secreat key of the minio instance.
     */
    private String secretKey;

    /**
     * Name of the root bucket.
     */
    private Path rootLocation;

    /**
     * If the input file size is unknown it is recommended to use a high number.
     * Source: https://docs.min.io/docs/java-client-api-reference.html#putObject
     */
    private static final int UNKNOWN_FILE_SIZE = 10485760;

    /**
     * Constructor.
     */
    public VideoStorageService() {
    }

    @PostConstruct
    /**
     * Initializes the VideoStorage.
     */
    private void init() {
        makeBucket(this.rootLocation);
    }

    /**
     * Creates a bucket in minio in the specified location if it doesn't already exist.
     *
     * @param location the location
     */
    private void makeBucket(final Path location) {
        MinioClient minioClient = buildClient();
        try {
            // Make uploads bucket if not exist.
            boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(location.toString())
                    .build());
            if (!found) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(location.toString())
                        .build());
            } else {
                String bucketExistsLog = String
                    .format("Bucket %s already exists.", location);
                LOGGER.info(bucketExistsLog);
            }
        } catch (InvalidKeyException
            | NoSuchAlgorithmException
            | IOException
            | MinioException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    /**
     * This method creates a MinioClient that this VideoStorage connects to.
     *
     * @return the MinioClient
     */
    private MinioClient buildClient() {
        return MinioClient.builder()
            .endpoint(this.minioUrl)
            .credentials(this.accessKey, this.secretKey)
            .build();
    }

    /**
     * Store a video file.
     *
     * @param file The video file to store.
     */
    public void store(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }
        storeFile(file);
    }

    /**
     * Actually stores the file.
     *
     * @param file The video file to store.
     */
    private void storeFile(final MultipartFile file) {
        MinioClient minioClient = buildClient();
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(rootLocation.toString())
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), -1, UNKNOWN_FILE_SIZE)
                    .build());

            String uploadSuccessLog = String
                .format(
                    "%s is successfully uploaded as object %s to bucket '%s'.",
                    file.getOriginalFilename(), file.getOriginalFilename(),
                    rootLocation.toString());
            LOGGER.info(uploadSuccessLog);
        } catch (InvalidKeyException
            | NoSuchAlgorithmException
            | IOException
            | MinioException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    public String getMinioUrl() {
        return minioUrl;
    }

    @Autowired
    public void setMinioUrl(@Value("${minio.url}") final String minioUrl) {
        this.minioUrl = minioUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    @Autowired
    public void setAccessKey(@Value("${minio.access-key}") final String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    @Autowired
    public void setSecretKey(@Value("${minio.secret-key}") final String secretKey) {
        this.secretKey = secretKey;
    }

    public Path getRootLocation() {
        return rootLocation;
    }

    @Autowired
    public void setRootLocation(@Value("${minio.root-location}") final Path rootLocation) {
        this.rootLocation = rootLocation;
    }
}
