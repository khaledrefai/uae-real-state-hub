package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.AgentProfile;
import com.yarmook.realstate.domain.AgentSite;
import com.yarmook.realstate.domain.SubscriptionPlan;
import com.yarmook.realstate.service.dto.AgentProfileDTO;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import com.yarmook.realstate.service.dto.SubscriptionPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgentSite} and its DTO {@link AgentSiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentSiteMapper extends EntityMapper<AgentSiteDTO, AgentSite> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "agentProfileId")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "subscriptionPlanId")
    AgentSiteDTO toDto(AgentSite s);

    @Named("agentProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentProfileDTO toDtoAgentProfileId(AgentProfile agentProfile);

    @Named("subscriptionPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionPlanDTO toDtoSubscriptionPlanId(SubscriptionPlan subscriptionPlan);
}
