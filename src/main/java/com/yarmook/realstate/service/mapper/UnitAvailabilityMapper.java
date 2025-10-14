package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.domain.UnitAvailability;
import com.yarmook.realstate.service.dto.PropertyDTO;
import com.yarmook.realstate.service.dto.UnitAvailabilityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UnitAvailability} and its DTO {@link UnitAvailabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface UnitAvailabilityMapper extends EntityMapper<UnitAvailabilityDTO, UnitAvailability> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    UnitAvailabilityDTO toDto(UnitAvailability s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
