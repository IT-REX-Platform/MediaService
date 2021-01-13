package de.uni_stuttgart.it_rex.media.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.uni_stuttgart.it_rex.media.web.rest.TestUtil;

public class MediaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MediaDTO.class);
        MediaDTO mediaDTO1 = new MediaDTO();
        mediaDTO1.setId(1L);
        MediaDTO mediaDTO2 = new MediaDTO();
        assertThat(mediaDTO1).isNotEqualTo(mediaDTO2);
        mediaDTO2.setId(mediaDTO1.getId());
        assertThat(mediaDTO1).isEqualTo(mediaDTO2);
        mediaDTO2.setId(2L);
        assertThat(mediaDTO1).isNotEqualTo(mediaDTO2);
        mediaDTO1.setId(null);
        assertThat(mediaDTO1).isNotEqualTo(mediaDTO2);
    }
}
