package de.uni_stuttgart.it_rex.media.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AudioMapperTest {

    private AudioMapper audioMapper;

    @BeforeEach
    public void setUp() {
        audioMapper = new AudioMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(audioMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(audioMapper.fromId(null)).isNull();
    }
}
