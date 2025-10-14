package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.domain.UnitBlock;
import com.yarmook.realstate.service.dto.PropertyDTO;
import com.yarmook.realstate.service.dto.UnitBlockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UnitBlock} and its DTO {@link UnitBlockDTO}.
 */
@Mapper(componentModel = "spring")
public interface UnitBlockMapper extends EntityMapper<UnitBlockDTO, UnitBlock> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    UnitBlockDTO toDto(UnitBlock s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
