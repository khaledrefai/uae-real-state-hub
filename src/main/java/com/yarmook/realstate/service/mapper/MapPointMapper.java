package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.MapPoint;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.service.dto.MapPointDTO;
import com.yarmook.realstate.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MapPoint} and its DTO {@link MapPointDTO}.
 */
@Mapper(componentModel = "spring")
public interface MapPointMapper extends EntityMapper<MapPointDTO, MapPoint> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    MapPointDTO toDto(MapPoint s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
