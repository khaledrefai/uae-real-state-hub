package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.AgentProfile;
import com.yarmook.realstate.repository.AgentProfileRepository;
import com.yarmook.realstate.service.criteria.AgentProfileCriteria;
import com.yarmook.realstate.service.dto.AgentProfileDTO;
import com.yarmook.realstate.service.mapper.AgentProfileMapper;
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
 * MongoDB-backed query service for {@link AgentProfile}.
 */
@Service
public class AgentProfileQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentProfileQueryService.class);

    private final AgentProfileRepository agentProfileRepository;
    private final AgentProfileMapper agentProfileMapper;

    public AgentProfileQueryService(AgentProfileRepository agentProfileRepository, AgentProfileMapper agentProfileMapper) {
        this.agentProfileRepository = agentProfileRepository;
        this.agentProfileMapper = agentProfileMapper;
    }

    public Page<AgentProfileDTO> findByCriteria(AgentProfileCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<AgentProfile> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(agentProfileMapper::toDto);
    }

    public long countByCriteria(AgentProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<AgentProfile> filterByCriteria(AgentProfileCriteria criteria) {
        return agentProfileRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(AgentProfile entity, AgentProfileCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getExternalUserId(), entity.getExternalUserId()) &&
            FilterMatcher.matches(criteria.getFullName(), entity.getFullName()) &&
            FilterMatcher.matches(criteria.getCompanyName(), entity.getCompanyName()) &&
            FilterMatcher.matches(criteria.getEmail(), entity.getEmail()) &&
            FilterMatcher.matches(criteria.getPhone(), entity.getPhone()) &&
            FilterMatcher.matches(criteria.getWhatsapp(), entity.getWhatsapp()) &&
            FilterMatcher.matches(criteria.getCountry(), entity.getCountry()) &&
            FilterMatcher.matches(criteria.getCity(), entity.getCity()) &&
            FilterMatcher.matches(criteria.getWebsite(), entity.getWebsite()) &&
            FilterMatcher.matches(criteria.getActive(), entity.getActive())
        );
    }
}
