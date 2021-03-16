package de.uni_stuttgart.it_rex.media.domain.written;

import de.uni_stuttgart.it_rex.media.util.written.ContentUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentTest {
    private static final UUID ID_1 = UUID.randomUUID();
    private static final UUID ID_2 = UUID.randomUUID();
    private static final String TITLE_1 = "AAAAAAAAAA";
    private static final String TITLE_2 = "BBBBBBBBBB";
    private static final LocalDate START_DATE_1 = LocalDate.MIN.plusDays(200);
    private static final LocalDate START_DATE_2 = LocalDate.MIN.plusDays(400);
    private static final LocalDate END_DATE_1 = LocalDate.MAX.minusDays(200);
    private static final LocalDate END_DATE_2 = LocalDate.MAX.minusDays(400);
    private static final UUID COURSE_ID_1 = UUID.randomUUID();
    private static final UUID COURSE_ID_2 = UUID.randomUUID();
    private static final UUID CHAPTER_ID_1 = UUID.randomUUID();
    private static final UUID CHAPTER_ID_2 = UUID.randomUUID();
    private static final UUID UPLOADER_ID_1 = UUID.randomUUID();
    private static final UUID UPLOADER_ID_2 = UUID.randomUUID();

    @Test
    void equals() {
        final Content content1 = new Video();
        content1.setId(ID_1);
        content1.setTitle(TITLE_1);
        content1.setStartDate(START_DATE_1);
        content1.setEndDate(END_DATE_1);
        content1.setCourseId(COURSE_ID_1);
        content1.setChapterId(CHAPTER_ID_1);
        content1.setUploaderId(UPLOADER_ID_1);

        final Content content2 = new Video();
        content2.setId(ID_2);
        content2.setTitle(TITLE_2);
        content2.setStartDate(START_DATE_2);
        content2.setEndDate(END_DATE_2);
        content2.setCourseId(COURSE_ID_2);
        content2.setChapterId(CHAPTER_ID_2);
        content2.setUploaderId(UPLOADER_ID_2);

        final Content content3 = new Video();
        content3.setId(ID_1);
        content3.setTitle(TITLE_1);
        content3.setStartDate(START_DATE_1);
        content3.setEndDate(END_DATE_1);
        content3.setCourseId(COURSE_ID_1);
        content3.setChapterId(CHAPTER_ID_1);
        content3.setUploaderId(UPLOADER_ID_1);

        ContentUtil.assertEquals(content1, content1);
        ContentUtil.assertEquals(content1, content3);
        ContentUtil.assertNotEquals(content1, content2);
        assertEquals(content1.hashCode(), content2.hashCode());
    }
}
