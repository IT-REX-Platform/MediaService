package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Content;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContentUtil {

    /**
     * Tests if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static boolean equals(final Content first, final Content second) {
        return Objects.equals(first.getId(), second.getId())
            && Objects.equals(first.getTitle(), second.getTitle())
            && Objects.equals(first.getStartDate(), second.getStartDate())
            && Objects.equals(first.getEndDate(), second.getEndDate())
            && Objects.equals(first.getCourseId(), second.getCourseId())
            && Objects.equals(first.getChapterId(), second.getChapterId())
            && Objects.equals(first.getUploaderId(), second.getUploaderId());
    }

    /**
     * Tests if two entities are equal.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Content first, final Content second) {
        assertTrue(equals(first, second));
    }

    /**
     * Tests if two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Content first, final Content second) {
        assertFalse(equals(first, second));
    }
}
