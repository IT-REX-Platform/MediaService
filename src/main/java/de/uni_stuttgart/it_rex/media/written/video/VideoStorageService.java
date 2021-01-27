package de.uni_stuttgart.it_rex.media.written.video;

import de.uni_stuttgart.it_rex.media.written.StorageException;
import de.uni_stuttgart.it_rex.media.written.StorageFileNotFoundException;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
     * The MinIO Client the media service connects to.
     */
    private final MinioClient minioClient;

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
    public VideoStorageService(
        @Value("${minio.access-key}") final String accessKey,
        @Value("${minio.secret-key}") final String secretKey) {
        this.rootLocation = Paths.get("videos");

        this.minioClient = MinioClient.builder()
            .endpoint("http://minio:8087")
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

    /**
     * Get the size of a stored file.
     * @param filename Name of the file to stat
     * @return Length of file
     */
    public long getLength(final String filename) {

        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(rootLocation.toString())
                    .object(filename)
                    .build());

            return stat.size();

        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (MinioException e) {
            throw new StorageException("Failed to read stored files", e);
        } catch (IOException e) {
        }

        return 0L;
    }

    /**
     * Get the contents of a stored file.
     * @param filename Name of file to load
     * @param offset Starting offset to read from
     * @param length How many bytes to read
     * @return *length* bytes of requested file starting from *offset* if file
     *         exists, null otherwise
     */
    public Resource loadAsResource(final String filename, final long offset,
                                   final long length) {

        // get object given the bucket and object name
        try {
            InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(rootLocation.toString())
                    .object(filename)
                    .offset(offset)
                    .length(length)
                    .build());

            // Read data from stream
            InputStreamResource
                resource = new InputStreamResource(stream, filename);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                    "Could not read file: " + filename);
            }
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (MinioException e) {
            throw new StorageException("Failed to read stored files", e);
        } catch (IOException e) {
        }

        return null;
    }
}
