package de.uni_stuttgart.it_rex.media.domain.written;

import de.uni_stuttgart.it_rex.media.util.written.DocumentUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    @Test
    void equalsVerifier() {
        Document document1 = new Document();
        document1.setId(FIRST_ID);
        Document document2 = new Document();
        document2.setId(document1.getId());
        DocumentUtil.equals(document1, document2);
        document2.setId(SECOND_ID);
        DocumentUtil.assertNotEquals(document1, document2);
        document1.setId(null);
        DocumentUtil.assertNotEquals(document1, document2);
        assertEquals(document1.hashCode(), document2.hashCode());
    }
}
