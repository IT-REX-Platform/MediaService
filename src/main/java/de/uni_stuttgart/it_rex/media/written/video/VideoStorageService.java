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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
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
     * The url to minio.
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
        // Empty because Spring autowiring is used for initialization
    }

    /**
     * Initializes the VideoStorage.
     */
    @PostConstruct
    private void init() {
        makeBucket(this.rootLocation);
    }

    /**
     * Creates a bucket in minio in the specified location
     * if it doesn't already exist.
     *
     * @param location the location
     */
    public void makeBucket(final Path location) {
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

    /**
     * Getter.
     *
     * @return the url where minio can be found
     */
    public final String getMinioUrl() {
        return minioUrl;
    }

    /**
     * Setter.
     *
     * @param newMinioUrl the url where minio can be found
     */
    @Autowired
    public final void setMinioUrl(
        @Value("${minio.url}") final String newMinioUrl) {
        this.minioUrl = newMinioUrl;
    }

    /**
     * Getter.
     *
     * @return the minio access key
     */
    public final String getAccessKey() {
        return accessKey;
    }

    /**
     * Setter.
     *
     * @param newAccessKey the minio access key
     */
    @Autowired
    public final void setAccessKey(
        @Value("${minio.access-key}") final String newAccessKey) {
        this.accessKey = newAccessKey;
    }

    /**
     * Getter.
     *
     * @return the minio secret key
     */
    public final String getSecretKey() {
        return secretKey;
    }

    /**
     * Setter.
     *
     * @param newSecretKey the minio secret key
     */
    @Autowired
    public final void setSecretKey(
        @Value("${minio.secret-key}") final String newSecretKey) {
        this.secretKey = newSecretKey;
    }

    /**
     * Getter.
     *
     * @return the root bucket of minio
     */
    public final Path getRootLocation() {
        return rootLocation;
    }

    /**
     * Setter.
     *
     * @param newLocation the root bucket of minio
     */
    @Autowired
    public final void setRootLocation(
        @Value("${minio.root-location}") final Path newLocation) {
        this.rootLocation = newLocation;
    }

    /**
     * Get the size of a stored file
     * @param filename Name of the file to stat
     * @return
     */
    public long getLength(String filename) {

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

        return 0l;
    }

    /**
     * Get the contents of a stored file
     * @param filename Name of file to load
     * @param offset Starting offset to read from
     * @param length How many bytes to read
     * @return *length* bytes of requested file starting from *offset* if file exists, null otherwise
     */
    public Resource loadAsResource(String filename, long offset, long length) {

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
                throw new StorageFileNotFoundException("Could not read file: " + filename);
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
