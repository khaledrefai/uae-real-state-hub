package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.MediaAsset;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.service.dto.MediaAssetDTO;
import com.yarmook.realstate.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MediaAsset} and its DTO {@link MediaAssetDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaAssetMapper extends EntityMapper<MediaAssetDTO, MediaAsset> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    MediaAssetDTO toDto(MediaAsset s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
