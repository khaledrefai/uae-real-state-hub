package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.service.AgentSiteQueryService;
import com.yarmook.realstate.service.criteria.AgentSiteCriteria;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * Public read-only access to agent storefront metadata for visitors.
 */
@RestController
@RequestMapping("/api/public/agent-sites")
public class PublicAgentSiteResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicAgentSiteResource.class);

    private final AgentSiteQueryService agentSiteQueryService;

    public PublicAgentSiteResource(AgentSiteQueryService agentSiteQueryService) {
        this.agentSiteQueryService = agentSiteQueryService;
    }

    /**
     * {@code GET /public/agent-sites} : list storefronts that should be visible to visitors.
     */
    @GetMapping("")
    public ResponseEntity<List<AgentSiteDTO>> getAgentSites(
        AgentSiteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        AgentSiteCriteria publicCriteria = applyPublicDefaults(criteria);
        LOG.debug("Public request to get AgentSites by criteria: {}", publicCriteria);
        Page<AgentSiteDTO> page = agentSiteQueryService.findByCriteria(publicCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /public/agent-sites/:slug} : resolve a storefront by slug for anonymous visitors.
     */
    @GetMapping("/{slug}")
    public ResponseEntity<AgentSiteDTO> getAgentSiteBySlug(@PathVariable("slug") String slug) {
        LOG.debug("Public request to get AgentSite by slug: {}", slug);
        AgentSiteCriteria criteria = new AgentSiteCriteria();
        criteria.slug().setEquals(slug);
        criteria.isActive().setEquals(true);
        criteria.distinct();
        Page<AgentSiteDTO> page = agentSiteQueryService.findByCriteria(criteria, PageRequest.of(0, 1));
        Optional<AgentSiteDTO> result = page.stream().findFirst();
        return ResponseUtil.wrapOrNotFound(result);
    }

    private AgentSiteCriteria applyPublicDefaults(AgentSiteCriteria criteria) {
        AgentSiteCriteria effective = Optional.ofNullable(criteria).map(AgentSiteCriteria::copy).orElseGet(AgentSiteCriteria::new);
        if (effective.optionalIsActive().isEmpty()) {
            effective.isActive().setEquals(true);
        }
        effective.distinct();
        return effective;
    }
}
