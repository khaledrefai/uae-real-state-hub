package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.AgentSite;
import com.yarmook.realstate.repository.AgentSiteRepository;
import com.yarmook.realstate.service.criteria.AgentSiteCriteria;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import com.yarmook.realstate.service.mapper.AgentSiteMapper;
import com.yarmook.realstate.service.util.FilterMatcher;
import com.yarmook.realstate.service.util.MongoPageUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * MongoDB-backed query service for {@link AgentSite}.
 */
@Service
public class AgentSiteQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentSiteQueryService.class);

    private final AgentSiteRepository agentSiteRepository;
    private final AgentSiteMapper agentSiteMapper;

    public AgentSiteQueryService(AgentSiteRepository agentSiteRepository, AgentSiteMapper agentSiteMapper) {
        this.agentSiteRepository = agentSiteRepository;
        this.agentSiteMapper = agentSiteMapper;
    }

    public Page<AgentSiteDTO> findByCriteria(AgentSiteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<AgentSite> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(agentSiteMapper::toDto);
    }

    public long countByCriteria(AgentSiteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<AgentSite> filterByCriteria(AgentSiteCriteria criteria) {
        return agentSiteRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(AgentSite entity, AgentSiteCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getSlug(), entity.getSlug()) &&
            FilterMatcher.matches(criteria.getDisplayName(), entity.getDisplayName()) &&
            FilterMatcher.matches(criteria.getTheme(), entity.getTheme()) &&
            FilterMatcher.matches(criteria.getPrimaryColor(), entity.getPrimaryColor()) &&
            FilterMatcher.matches(criteria.getSecondaryColor(), entity.getSecondaryColor()) &&
            FilterMatcher.matches(criteria.getLogoUrl(), entity.getLogoUrl()) &&
            FilterMatcher.matches(criteria.getContactEmail(), entity.getContactEmail()) &&
            FilterMatcher.matches(criteria.getContactPhone(), entity.getContactPhone()) &&
            FilterMatcher.matches(criteria.getContactWhatsapp(), entity.getContactWhatsapp()) &&
            FilterMatcher.matches(criteria.getDomain(), entity.getDomain()) &&
            FilterMatcher.matches(criteria.getIsActive(), entity.getIsActive()) &&
            FilterMatcher.matchesRange(criteria.getOwnerId(), entity.getOwner() != null ? entity.getOwner().getId() : null) &&
            FilterMatcher.matchesRange(criteria.getPlanId(), entity.getPlan() != null ? entity.getPlan().getId() : null)
        );
    }
}
