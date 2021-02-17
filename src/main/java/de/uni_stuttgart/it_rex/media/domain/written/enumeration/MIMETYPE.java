package de.uni_stuttgart.it_rex.media.domain.written.enumeration;

/**
 * The MIMETYPE enumeration.
 */
public enum MIMETYPE {
    /**
     * MP3.
     */
    AUDIO_MPEG("audio/mpeg"),
    /**
     * GIF.
     */
    IMAGE_GIF("image/gif"),
    /**
     * JPEG.
     */
    IMAGE_JPEG("image/jpeg"),
    /**
     * PNG.
     */
    IMAGE_PNG("image/png"),
    /**
     * SVG.
     */
    IMAGE_SVG("image/svg"),
    /**
     * MP4.
     */
    VIDEO_MP4("video/mp4"),
    /**
     * PDF.
     */
    APPLICATION_PDF("application/pdf");

    /**
     * The value.
     */
    private final String value;

    /**
     * Sets the value.
     * @param newValue the value.
     */
    MIMETYPE(final String newValue) {
        this.value = newValue;
    }

    /**
     * Getter.
     * @return the value.
     */
    public String getValue() {
        return value;
    }
}
