package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Image;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ImageUtil {

    /**
     * Checks if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static boolean equals(final Image first, final Image second) {
        return MediaUtil.equals(first, second)
            && Objects.equals(first.getHeight(), second.getHeight())
            && Objects.equals(first.getWidth(), second.getWidth());
    }

    /**
     * Asserts that two entities are equal.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Image first, final Image second) {
        assertTrue(equals(first, second));
    }

    /**
     * Asserts that two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Image first, final Image second) {
        assertFalse(equals(first, second));
    }
}
