package de.uni_stuttgart.it_rex.media.util.written;

import de.uni_stuttgart.it_rex.media.domain.written.Video;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VideoUtil {
    /**
     * Creates a random entity.
     *
     * @return the entity
     */
    public static Video createVideo() {
        final Video video = new Video();
        video.setTitle(StringUtil.generateRandomString(10, 50));
        video.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        video.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        video.setCourseId(UUID.randomUUID());
        video.setChapterId(UUID.randomUUID());
        video.setUploaderId(UUID.randomUUID());
        video.setMimeType(EnumUtil.generateRandomMimeType());
        video.setWidth(NumbersUtil.generateRandomInteger(10, 1080));
        video.setHeight(NumbersUtil.generateRandomInteger(10, 1920));
        video.setLength(NumbersUtil.generateRandomLong(10, 9999));
        return video;
    }

    /**
     * Creates a List of random entities.
     *
     * @param number the length of the list
     * @return the DTOs
     */
    public static List<Video> createVideos(final int number) {
        return IntStream.range(0, number).mapToObj(i -> createVideo()).collect(Collectors.toList());
    }

    public static boolean equals(final Video first, final Video second) {
        return MediaUtil.equals(first, second)
            && Objects.equals(first.getLength(), second.getLength())
            && Objects.equals(first.getHeight(), second.getHeight())
            && Objects.equals(first.getWidth(), second.getWidth());
    }

    /**
     * Tests if two entities are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void assertEquals(final Video first, final Video second) {
        assertTrue(equals(first, second));
    }

    /**
     * Tests if two entities are not equal.
     *
     * @param first
     * @param second
     */
    public static void assertNotEquals(final Video first, final Video second) {
        assertFalse(equals(first, second));
    }
}
