package de.uni_stuttgart.it_rex.media.service.mapper;

import de.uni_stuttgart.it_rex.media.domain.written.Content;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ContentMapper {

  /**
   * Updates an entity from another entity.
   *
   * @param update   the update
   * @param toUpdate the updated entity.
   */
  @BeanMapping(nullValuePropertyMappingStrategy =
      NullValuePropertyMappingStrategy.IGNORE)
  void updateContentFromContent(
      Content update, @MappingTarget Content toUpdate);
}
