package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Document;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DocumentUtil {

    /**
     * Checks if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static boolean equals(final Document first, final Document second) {
        return ResourceUtil.equals(first, second);
    }

    /**
     * Asserts that two entities are equal.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Document first, final Document second) {
        assertTrue(equals(first, second));
    }

    /**
     * Asserts that two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Document first, final Document second) {
        assertFalse(equals(first, second));
    }
}
