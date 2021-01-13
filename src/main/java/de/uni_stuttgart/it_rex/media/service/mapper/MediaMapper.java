package de.uni_stuttgart.it_rex.media.service.mapper;


import de.uni_stuttgart.it_rex.media.domain.*;
import de.uni_stuttgart.it_rex.media.service.dto.MediaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {



    default Media fromId(Long id) {
        if (id == null) {
            return null;
        }
        Media media = new Media();
        media.setId(id);
        return media;
    }
}
