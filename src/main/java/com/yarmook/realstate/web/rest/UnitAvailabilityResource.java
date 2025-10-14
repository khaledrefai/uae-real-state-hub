package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.UnitAvailabilityRepository;
import com.yarmook.realstate.service.UnitAvailabilityQueryService;
import com.yarmook.realstate.service.UnitAvailabilityService;
import com.yarmook.realstate.service.criteria.UnitAvailabilityCriteria;
import com.yarmook.realstate.service.dto.UnitAvailabilityDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yarmook.realstate.domain.UnitAvailability}.
 */
@RestController
@RequestMapping("/api/unit-availabilities")
public class UnitAvailabilityResource {

    private static final Logger LOG = LoggerFactory.getLogger(UnitAvailabilityResource.class);

    private static final String ENTITY_NAME = "unitAvailability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitAvailabilityService unitAvailabilityService;

    private final UnitAvailabilityRepository unitAvailabilityRepository;

    private final UnitAvailabilityQueryService unitAvailabilityQueryService;

    public UnitAvailabilityResource(
        UnitAvailabilityService unitAvailabilityService,
        UnitAvailabilityRepository unitAvailabilityRepository,
        UnitAvailabilityQueryService unitAvailabilityQueryService
    ) {
        this.unitAvailabilityService = unitAvailabilityService;
        this.unitAvailabilityRepository = unitAvailabilityRepository;
        this.unitAvailabilityQueryService = unitAvailabilityQueryService;
    }

    /**
     * {@code POST  /unit-availabilities} : Create a new unitAvailability.
     *
     * @param unitAvailabilityDTO the unitAvailabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitAvailabilityDTO, or with status {@code 400 (Bad Request)} if the unitAvailability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UnitAvailabilityDTO> createUnitAvailability(@Valid @RequestBody UnitAvailabilityDTO unitAvailabilityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UnitAvailability : {}", unitAvailabilityDTO);
        if (unitAvailabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitAvailability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        unitAvailabilityDTO = unitAvailabilityService.save(unitAvailabilityDTO);
        return ResponseEntity.created(new URI("/api/unit-availabilities/" + unitAvailabilityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, unitAvailabilityDTO.getId().toString()))
            .body(unitAvailabilityDTO);
    }

    /**
     * {@code PUT  /unit-availabilities/:id} : Updates an existing unitAvailability.
     *
     * @param id the id of the unitAvailabilityDTO to save.
     * @param unitAvailabilityDTO the unitAvailabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitAvailabilityDTO,
     * or with status {@code 400 (Bad Request)} if the unitAvailabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitAvailabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnitAvailabilityDTO> updateUnitAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitAvailabilityDTO unitAvailabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UnitAvailability : {}, {}", id, unitAvailabilityDTO);
        if (unitAvailabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitAvailabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitAvailabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        unitAvailabilityDTO = unitAvailabilityService.update(unitAvailabilityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitAvailabilityDTO.getId().toString()))
            .body(unitAvailabilityDTO);
    }

    /**
     * {@code PATCH  /unit-availabilities/:id} : Partial updates given fields of an existing unitAvailability, field will ignore if it is null
     *
     * @param id the id of the unitAvailabilityDTO to save.
     * @param unitAvailabilityDTO the unitAvailabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitAvailabilityDTO,
     * or with status {@code 400 (Bad Request)} if the unitAvailabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitAvailabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitAvailabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitAvailabilityDTO> partialUpdateUnitAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitAvailabilityDTO unitAvailabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UnitAvailability partially : {}, {}", id, unitAvailabilityDTO);
        if (unitAvailabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitAvailabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitAvailabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitAvailabilityDTO> result = unitAvailabilityService.partialUpdate(unitAvailabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitAvailabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-availabilities} : get all the unitAvailabilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitAvailabilities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UnitAvailabilityDTO>> getAllUnitAvailabilities(
        UnitAvailabilityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UnitAvailabilities by criteria: {}", criteria);

        Page<UnitAvailabilityDTO> page = unitAvailabilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-availabilities/count} : count all the unitAvailabilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUnitAvailabilities(UnitAvailabilityCriteria criteria) {
        LOG.debug("REST request to count UnitAvailabilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(unitAvailabilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /unit-availabilities/:id} : get the "id" unitAvailability.
     *
     * @param id the id of the unitAvailabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitAvailabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitAvailabilityDTO> getUnitAvailability(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UnitAvailability : {}", id);
        Optional<UnitAvailabilityDTO> unitAvailabilityDTO = unitAvailabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitAvailabilityDTO);
    }

    /**
     * {@code DELETE  /unit-availabilities/:id} : delete the "id" unitAvailability.
     *
     * @param id the id of the unitAvailabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitAvailability(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UnitAvailability : {}", id);
        unitAvailabilityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
