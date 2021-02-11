package de.uni_stuttgart.it_rex.media.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DocumentMapperTest {

    private DocumentMapper documentMapper;

    @BeforeEach
    public void setUp() {
        documentMapper = new DocumentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(documentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(documentMapper.fromId(null)).isNull();
    }
}
