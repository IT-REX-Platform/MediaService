package de.uni_stuttgart.it_rex.media.service.mapper;


import de.uni_stuttgart.it_rex.media.domain.*;
import de.uni_stuttgart.it_rex.media.service.dto.AudioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Audio} and its DTO {@link AudioDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AudioMapper extends EntityMapper<AudioDTO, Audio> {



    default Audio fromId(Long id) {
        if (id == null) {
            return null;
        }
        Audio audio = new Audio();
        audio.setId(id);
        return audio;
    }
}
