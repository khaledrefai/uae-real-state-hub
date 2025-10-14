package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.PropertyMarker;
import com.yarmook.realstate.service.dto.PropertyMarkerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PropertyMarker} and its DTO {@link PropertyMarkerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PropertyMarkerMapper extends EntityMapper<PropertyMarkerDTO, PropertyMarker> {}
