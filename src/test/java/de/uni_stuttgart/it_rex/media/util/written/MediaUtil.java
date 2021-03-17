package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Media;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class MediaUtil {

    /**
     * Checks if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static boolean equals(final Media first, final Media second) {
        return ResourceUtil.equals(first, second);
    }

    /**
     * Asserts that two entities are equal.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Media first, final Media second) {
        assertTrue(equals(first, second));
    }

    /**
     * Asserts that two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Media first, final Media second) {
        assertFalse(equals(first, second));
    }
}
