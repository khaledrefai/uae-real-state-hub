package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.AgentSite;
import com.yarmook.realstate.domain.BillingSubscription;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import com.yarmook.realstate.service.dto.BillingSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillingSubscription} and its DTO {@link BillingSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BillingSubscriptionMapper extends EntityMapper<BillingSubscriptionDTO, BillingSubscription> {
    @Mapping(target = "site", source = "site", qualifiedByName = "agentSiteId")
    BillingSubscriptionDTO toDto(BillingSubscription s);

    @Named("agentSiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentSiteDTO toDtoAgentSiteId(AgentSite agentSite);
}
