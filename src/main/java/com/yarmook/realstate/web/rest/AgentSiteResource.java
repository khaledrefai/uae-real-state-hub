package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.AgentSiteRepository;
import com.yarmook.realstate.security.AuthoritiesConstants;
import com.yarmook.realstate.service.AgentSiteQueryService;
import com.yarmook.realstate.service.AgentSiteService;
import com.yarmook.realstate.service.criteria.AgentSiteCriteria;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import com.yarmook.realstate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yarmook.realstate.domain.AgentSite}.
 */
@RestController
@RequestMapping("/api/agent-sites")
@PreAuthorize("hasAnyAuthority('" + AuthoritiesConstants.ADMIN + "', '" + AuthoritiesConstants.AGENT + "')")
public class AgentSiteResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgentSiteResource.class);

    private static final String ENTITY_NAME = "agentSite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentSiteService agentSiteService;

    private final AgentSiteRepository agentSiteRepository;

    private final AgentSiteQueryService agentSiteQueryService;

    public AgentSiteResource(
        AgentSiteService agentSiteService,
        AgentSiteRepository agentSiteRepository,
        AgentSiteQueryService agentSiteQueryService
    ) {
        this.agentSiteService = agentSiteService;
        this.agentSiteRepository = agentSiteRepository;
        this.agentSiteQueryService = agentSiteQueryService;
    }

    /**
     * {@code POST  /agent-sites} : Create a new agentSite.
     *
     * @param agentSiteDTO the agentSiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentSiteDTO, or with status {@code 400 (Bad Request)} if the agentSite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgentSiteDTO> createAgentSite(@Valid @RequestBody AgentSiteDTO agentSiteDTO) throws URISyntaxException {
        LOG.debug("REST request to save AgentSite : {}", agentSiteDTO);
        if (agentSiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentSite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agentSiteDTO = agentSiteService.save(agentSiteDTO);
        return ResponseEntity.created(new URI("/api/agent-sites/" + agentSiteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agentSiteDTO.getId().toString()))
            .body(agentSiteDTO);
    }

    /**
     * {@code PUT  /agent-sites/:id} : Updates an existing agentSite.
     *
     * @param id the id of the agentSiteDTO to save.
     * @param agentSiteDTO the agentSiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentSiteDTO,
     * or with status {@code 400 (Bad Request)} if the agentSiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentSiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgentSiteDTO> updateAgentSite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgentSiteDTO agentSiteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AgentSite : {}, {}", id, agentSiteDTO);
        if (agentSiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentSiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentSiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agentSiteDTO = agentSiteService.update(agentSiteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentSiteDTO.getId().toString()))
            .body(agentSiteDTO);
    }

    /**
     * {@code PATCH  /agent-sites/:id} : Partial updates given fields of an existing agentSite, field will ignore if it is null
     *
     * @param id the id of the agentSiteDTO to save.
     * @param agentSiteDTO the agentSiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentSiteDTO,
     * or with status {@code 400 (Bad Request)} if the agentSiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agentSiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agentSiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgentSiteDTO> partialUpdateAgentSite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgentSiteDTO agentSiteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AgentSite partially : {}, {}", id, agentSiteDTO);
        if (agentSiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentSiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentSiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgentSiteDTO> result = agentSiteService.partialUpdate(agentSiteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentSiteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agent-sites} : get all the agentSites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentSites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgentSiteDTO>> getAllAgentSites(
        AgentSiteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AgentSites by criteria: {}", criteria);

        Page<AgentSiteDTO> page = agentSiteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agent-sites/count} : count all the agentSites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgentSites(AgentSiteCriteria criteria) {
        LOG.debug("REST request to count AgentSites by criteria: {}", criteria);
        return ResponseEntity.ok().body(agentSiteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agent-sites/:id} : get the "id" agentSite.
     *
     * @param id the id of the agentSiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentSiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgentSiteDTO> getAgentSite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AgentSite : {}", id);
        Optional<AgentSiteDTO> agentSiteDTO = agentSiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentSiteDTO);
    }

    /**
     * {@code DELETE  /agent-sites/:id} : delete the "id" agentSite.
     *
     * @param id the id of the agentSiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgentSite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AgentSite : {}", id);
        agentSiteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
