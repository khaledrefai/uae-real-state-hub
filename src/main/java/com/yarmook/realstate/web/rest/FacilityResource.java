package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.FacilityRepository;
import com.yarmook.realstate.service.FacilityQueryService;
import com.yarmook.realstate.service.FacilityService;
import com.yarmook.realstate.service.criteria.FacilityCriteria;
import com.yarmook.realstate.service.dto.FacilityDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.Facility}.
 */
@RestController
@RequestMapping("/api/facilities")
public class FacilityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityResource.class);

    private static final String ENTITY_NAME = "facility";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacilityService facilityService;

    private final FacilityRepository facilityRepository;

    private final FacilityQueryService facilityQueryService;

    public FacilityResource(
        FacilityService facilityService,
        FacilityRepository facilityRepository,
        FacilityQueryService facilityQueryService
    ) {
        this.facilityService = facilityService;
        this.facilityRepository = facilityRepository;
        this.facilityQueryService = facilityQueryService;
    }

    /**
     * {@code POST  /facilities} : Create a new facility.
     *
     * @param facilityDTO the facilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityDTO, or with status {@code 400 (Bad Request)} if the facility has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FacilityDTO> createFacility(@Valid @RequestBody FacilityDTO facilityDTO) throws URISyntaxException {
        LOG.debug("REST request to save Facility : {}", facilityDTO);
        if (facilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new facility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        facilityDTO = facilityService.save(facilityDTO);
        return ResponseEntity.created(new URI("/api/facilities/" + facilityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, facilityDTO.getId().toString()))
            .body(facilityDTO);
    }

    /**
     * {@code PUT  /facilities/:id} : Updates an existing facility.
     *
     * @param id the id of the facilityDTO to save.
     * @param facilityDTO the facilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityDTO,
     * or with status {@code 400 (Bad Request)} if the facilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacilityDTO> updateFacility(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FacilityDTO facilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Facility : {}, {}", id, facilityDTO);
        if (facilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        facilityDTO = facilityService.update(facilityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityDTO.getId().toString()))
            .body(facilityDTO);
    }

    /**
     * {@code PATCH  /facilities/:id} : Partial updates given fields of an existing facility, field will ignore if it is null
     *
     * @param id the id of the facilityDTO to save.
     * @param facilityDTO the facilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityDTO,
     * or with status {@code 400 (Bad Request)} if the facilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityDTO> partialUpdateFacility(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FacilityDTO facilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Facility partially : {}, {}", id, facilityDTO);
        if (facilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityDTO> result = facilityService.partialUpdate(facilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facilities} : get all the facilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facilities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FacilityDTO>> getAllFacilities(
        FacilityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Facilities by criteria: {}", criteria);

        Page<FacilityDTO> page = facilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facilities/count} : count all the facilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFacilities(FacilityCriteria criteria) {
        LOG.debug("REST request to count Facilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(facilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /facilities/:id} : get the "id" facility.
     *
     * @param id the id of the facilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getFacility(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Facility : {}", id);
        Optional<FacilityDTO> facilityDTO = facilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityDTO);
    }

    /**
     * {@code DELETE  /facilities/:id} : delete the "id" facility.
     *
     * @param id the id of the facilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Facility : {}", id);
        facilityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
