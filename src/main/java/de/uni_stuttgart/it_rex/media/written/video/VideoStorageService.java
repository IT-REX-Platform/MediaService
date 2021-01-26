package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.written.StorageException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final String minioUrl;

    /**
     * The MinIO Client the media service connects to.
     */
    private MinioClient minioClient;

    /**
     * Name of the root bucket.
     */
    private final Path rootLocation;

    /**
     * If the input file size is unknown it is recommended to use a high number.
     * Source: https://docs.min.io/docs/java-client-api-reference.html#putObject
     */
    private static final int UNKNOWN_FILE_SIZE = 10485760;

    /**
     * Create a minioClient with the MinIO server playground,
     * its access key and secret key.
     *
     * @param accessKey The access key for the MinIO client.
     * @param secretKey The secret key for the MinIO client.
     */
    @Autowired
    public VideoStorageService(@Value("${minio.url}") final String minioUrl,
                               @Value("${minio.access-key}") final String accessKey,
                               @Value("${minio.secret-key}") final String secretKey) {
        this.minioUrl = minioUrl;
        this.rootLocation = Paths.get("videos");

        connect(this.minioUrl, accessKey, secretKey);
    }

    /**
     * Actually creates the minioClient
     *
     * @param minioUrl  the minioUrl
     * @param accessKey The access key for the MinIO client.
     * @param secretKey The secret key for the MinIO client.
     */
    @NotNull
    public void connect(final String minioUrl, final String accessKey, final String secretKey) {
        this.minioClient = MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .build();

        try {
            // Make uploads bucket if not exist.
            boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(rootLocation.toString())
                    .build());
            if (!found) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(rootLocation.toString())
                        .build());
            } else {
                String bucketExistsLog = String
                    .format("Bucket %s already exists.",
                        rootLocation.toString());
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

    private void storeFile(final MultipartFile file) {
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
}
