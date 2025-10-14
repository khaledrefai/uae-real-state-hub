package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.Facility;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.service.dto.FacilityDTO;
import com.yarmook.realstate.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facility} and its DTO {@link FacilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacilityMapper extends EntityMapper<FacilityDTO, Facility> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    FacilityDTO toDto(Facility s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
