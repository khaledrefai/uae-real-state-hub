package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.domain.PropertyMarker;
import com.yarmook.realstate.service.dto.PropertyDTO;
import com.yarmook.realstate.service.dto.PropertyMarkerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Property} and its DTO {@link PropertyDTO}.
 */
@Mapper(componentModel = "spring")
public interface PropertyMapper extends EntityMapper<PropertyDTO, Property> {
    @Mapping(target = "marker", source = "marker", qualifiedByName = "propertyMarkerId")
    PropertyDTO toDto(Property s);

    @Named("propertyMarkerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyMarkerDTO toDtoPropertyMarkerId(PropertyMarker propertyMarker);
}
