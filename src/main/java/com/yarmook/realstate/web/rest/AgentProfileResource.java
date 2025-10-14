package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.AgentProfileRepository;
import com.yarmook.realstate.security.AuthoritiesConstants;
import com.yarmook.realstate.service.AgentProfileQueryService;
import com.yarmook.realstate.service.AgentProfileService;
import com.yarmook.realstate.service.criteria.AgentProfileCriteria;
import com.yarmook.realstate.service.dto.AgentProfileDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.AgentProfile}.
 */
@RestController
@RequestMapping("/api/agent-profiles")
@PreAuthorize("hasAnyAuthority('" + AuthoritiesConstants.ADMIN + "', '" + AuthoritiesConstants.AGENT + "')")
public class AgentProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgentProfileResource.class);

    private static final String ENTITY_NAME = "agentProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentProfileService agentProfileService;

    private final AgentProfileRepository agentProfileRepository;

    private final AgentProfileQueryService agentProfileQueryService;

    public AgentProfileResource(
        AgentProfileService agentProfileService,
        AgentProfileRepository agentProfileRepository,
        AgentProfileQueryService agentProfileQueryService
    ) {
        this.agentProfileService = agentProfileService;
        this.agentProfileRepository = agentProfileRepository;
        this.agentProfileQueryService = agentProfileQueryService;
    }

    /**
     * {@code POST  /agent-profiles} : Create a new agentProfile.
     *
     * @param agentProfileDTO the agentProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentProfileDTO, or with status {@code 400 (Bad Request)} if the agentProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgentProfileDTO> createAgentProfile(@Valid @RequestBody AgentProfileDTO agentProfileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AgentProfile : {}", agentProfileDTO);
        if (agentProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agentProfileDTO = agentProfileService.save(agentProfileDTO);
        return ResponseEntity.created(new URI("/api/agent-profiles/" + agentProfileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agentProfileDTO.getId().toString()))
            .body(agentProfileDTO);
    }

    /**
     * {@code PUT  /agent-profiles/:id} : Updates an existing agentProfile.
     *
     * @param id the id of the agentProfileDTO to save.
     * @param agentProfileDTO the agentProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentProfileDTO,
     * or with status {@code 400 (Bad Request)} if the agentProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgentProfileDTO> updateAgentProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgentProfileDTO agentProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AgentProfile : {}, {}", id, agentProfileDTO);
        if (agentProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agentProfileDTO = agentProfileService.update(agentProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentProfileDTO.getId().toString()))
            .body(agentProfileDTO);
    }

    /**
     * {@code PATCH  /agent-profiles/:id} : Partial updates given fields of an existing agentProfile, field will ignore if it is null
     *
     * @param id the id of the agentProfileDTO to save.
     * @param agentProfileDTO the agentProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentProfileDTO,
     * or with status {@code 400 (Bad Request)} if the agentProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agentProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agentProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgentProfileDTO> partialUpdateAgentProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgentProfileDTO agentProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AgentProfile partially : {}, {}", id, agentProfileDTO);
        if (agentProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgentProfileDTO> result = agentProfileService.partialUpdate(agentProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agent-profiles} : get all the agentProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentProfiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgentProfileDTO>> getAllAgentProfiles(
        AgentProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AgentProfiles by criteria: {}", criteria);

        Page<AgentProfileDTO> page = agentProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agent-profiles/count} : count all the agentProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgentProfiles(AgentProfileCriteria criteria) {
        LOG.debug("REST request to count AgentProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(agentProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agent-profiles/:id} : get the "id" agentProfile.
     *
     * @param id the id of the agentProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgentProfileDTO> getAgentProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AgentProfile : {}", id);
        Optional<AgentProfileDTO> agentProfileDTO = agentProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentProfileDTO);
    }

    /**
     * {@code DELETE  /agent-profiles/:id} : delete the "id" agentProfile.
     *
     * @param id the id of the agentProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgentProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AgentProfile : {}", id);
        agentProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
