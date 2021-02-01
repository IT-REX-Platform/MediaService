package de.uni_stuttgart.it_rex.media.written;

public class StorageException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public StorageException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause   The cause for the exception.
     */
    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
