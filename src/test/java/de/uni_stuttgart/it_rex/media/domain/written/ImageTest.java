package de.uni_stuttgart.it_rex.media.domain.written;

import de.uni_stuttgart.it_rex.media.util.written.ImageUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();
    private static final Integer FIRST_WIDTH = 23423;
    private static final Integer SECOND_WIDTH = 2999423;
    private static final Integer FIRST_HEIGHT = 2342123;
    private static final Integer SECOND_HEIGHT = 23345423;

    @Test
    public void equalsVerifier() {
        Image image1 = new Image();
        image1.setId(FIRST_ID);
        image1.setWidth(FIRST_WIDTH);
        image1.setHeight(FIRST_HEIGHT);

        Image image2 = new Image();
        image2.setId(SECOND_ID);
        image2.setWidth(SECOND_WIDTH);
        image2.setHeight(SECOND_HEIGHT);

        Image image3 = new Image();
        image3.setId(FIRST_ID);
        image3.setWidth(FIRST_WIDTH);
        image3.setHeight(FIRST_HEIGHT);

        ImageUtil.assertEquals(image1, image1);
        ImageUtil.assertEquals(image1, image3);
        ImageUtil.assertNotEquals(image1, image2);
        assertEquals(image1.hashCode(), image2.hashCode());
    }
}
