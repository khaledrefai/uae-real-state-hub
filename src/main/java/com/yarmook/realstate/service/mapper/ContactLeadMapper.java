package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.AgentSite;
import com.yarmook.realstate.domain.ContactLead;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import com.yarmook.realstate.service.dto.ContactLeadDTO;
import com.yarmook.realstate.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactLead} and its DTO {@link ContactLeadDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactLeadMapper extends EntityMapper<ContactLeadDTO, ContactLead> {
    @Mapping(target = "site", source = "site", qualifiedByName = "agentSiteId")
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    ContactLeadDTO toDto(ContactLead s);

    @Named("agentSiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentSiteDTO toDtoAgentSiteId(AgentSite agentSite);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
