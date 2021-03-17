package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Resource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ResourceUtil {

    /**
     * Checks if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static boolean equals(final Resource first, final Resource second) {
        return ContentUtil.equals(first, second)
            && Objects.equals(first.getMimeType(), second.getMimeType());
    }

    /**
     * Asserts that two entities are equal.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Resource first, final Resource second) {
        assertTrue(equals(first, second));
    }

    /**
     * Asserts that two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Resource first, final Resource second) {
        assertFalse(equals(first, second));
    }
}
