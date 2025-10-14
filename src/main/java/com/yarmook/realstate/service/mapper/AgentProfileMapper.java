package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.AgentProfile;
import com.yarmook.realstate.service.dto.AgentProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentProfile} and its DTO {@link AgentProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentProfileMapper extends EntityMapper<AgentProfileDTO, AgentProfile> {}
