package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Audio;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AudioUtil {

    public static boolean equals(final Audio first, final Audio second) {
        return MediaUtil.equals(first, second)
            && Objects.equals(first.getLength(), second.getLength());
    }

    /**
     * Tests if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Audio first, final Audio second) {
        assertTrue(equals(first, second));
    }

    /**
     * Tests if two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Audio first, final Audio second) {
        assertFalse(equals(first, second));
    }
}
