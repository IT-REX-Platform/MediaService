package de.uni_stuttgart.it_rex.media.service.mapper;


import de.uni_stuttgart.it_rex.media.domain.*;
import de.uni_stuttgart.it_rex.media.service.dto.VideoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Video} and its DTO {@link VideoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VideoMapper extends EntityMapper<VideoDTO, Video> {



    default Video fromId(Long id) {
        if (id == null) {
            return null;
        }
        Video video = new Video();
        video.setId(id);
        return video;
    }
}
