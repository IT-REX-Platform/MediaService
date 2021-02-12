package de.uni_stuttgart.it_rex.media.domain.written.enumeration;

/**
 * The MIMETYPE enumeration.
 */
public enum MIMETYPE {
    AUDIO_MPEG("audio/mpeg"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_SVG("image/svg"),
    VIDEO_MP4("video/mp4");

    private final String value;


    MIMETYPE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
