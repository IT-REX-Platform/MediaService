package de.uni_stuttgart.it_rex.media.service.written;

public class StorageFileNotFoundException extends StorageException {

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public StorageFileNotFoundException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause   The cause for the exception.
     */
    public StorageFileNotFoundException(final String message,
                                        final Throwable cause) {
        super(message, cause);
    }
}
