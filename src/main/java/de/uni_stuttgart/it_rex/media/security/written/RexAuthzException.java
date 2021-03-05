package de.uni_stuttgart.it_rex.media.security.written;

public class RexAuthzException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public RexAuthzException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause   The cause for the exception.
     */
    public RexAuthzException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
