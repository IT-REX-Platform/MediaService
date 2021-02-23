package de.uni_stuttgart.it_rex.media.service.mapper.written;

import de.uni_stuttgart.it_rex.media.domain.written.Video;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    /**
     * Updates an entity from another entity.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateVideoFromVideo(Video update, @MappingTarget Video toUpdate);
}
