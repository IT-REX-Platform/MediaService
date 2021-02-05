package de.uni_stuttgart.it_rex.media.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.uni_stuttgart.it_rex.media.web.rest.TestUtil;

public class AudioDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AudioDTO.class);
        AudioDTO audioDTO1 = new AudioDTO();
        audioDTO1.setId(1L);
        AudioDTO audioDTO2 = new AudioDTO();
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
        audioDTO2.setId(audioDTO1.getId());
        assertThat(audioDTO1).isEqualTo(audioDTO2);
        audioDTO2.setId(2L);
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
        audioDTO1.setId(null);
        assertThat(audioDTO1).isNotEqualTo(audioDTO2);
    }
}
