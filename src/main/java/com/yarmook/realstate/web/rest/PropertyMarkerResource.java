package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.PropertyMarkerRepository;
import com.yarmook.realstate.service.PropertyMarkerQueryService;
import com.yarmook.realstate.service.PropertyMarkerService;
import com.yarmook.realstate.service.criteria.PropertyMarkerCriteria;
import com.yarmook.realstate.service.dto.PropertyMarkerDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.PropertyMarker}.
 */
@RestController
@RequestMapping("/api/property-markers")
public class PropertyMarkerResource {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyMarkerResource.class);

    private static final String ENTITY_NAME = "propertyMarker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyMarkerService propertyMarkerService;

    private final PropertyMarkerRepository propertyMarkerRepository;

    private final PropertyMarkerQueryService propertyMarkerQueryService;

    public PropertyMarkerResource(
        PropertyMarkerService propertyMarkerService,
        PropertyMarkerRepository propertyMarkerRepository,
        PropertyMarkerQueryService propertyMarkerQueryService
    ) {
        this.propertyMarkerService = propertyMarkerService;
        this.propertyMarkerRepository = propertyMarkerRepository;
        this.propertyMarkerQueryService = propertyMarkerQueryService;
    }

    /**
     * {@code POST  /property-markers} : Create a new propertyMarker.
     *
     * @param propertyMarkerDTO the propertyMarkerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyMarkerDTO, or with status {@code 400 (Bad Request)} if the propertyMarker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PropertyMarkerDTO> createPropertyMarker(@Valid @RequestBody PropertyMarkerDTO propertyMarkerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PropertyMarker : {}", propertyMarkerDTO);
        if (propertyMarkerDTO.getId() != null) {
            throw new BadRequestAlertException("A new propertyMarker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        propertyMarkerDTO = propertyMarkerService.save(propertyMarkerDTO);
        return ResponseEntity.created(new URI("/api/property-markers/" + propertyMarkerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, propertyMarkerDTO.getId().toString()))
            .body(propertyMarkerDTO);
    }

    /**
     * {@code PUT  /property-markers/:id} : Updates an existing propertyMarker.
     *
     * @param id the id of the propertyMarkerDTO to save.
     * @param propertyMarkerDTO the propertyMarkerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyMarkerDTO,
     * or with status {@code 400 (Bad Request)} if the propertyMarkerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyMarkerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PropertyMarkerDTO> updatePropertyMarker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PropertyMarkerDTO propertyMarkerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PropertyMarker : {}, {}", id, propertyMarkerDTO);
        if (propertyMarkerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyMarkerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyMarkerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        propertyMarkerDTO = propertyMarkerService.update(propertyMarkerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyMarkerDTO.getId().toString()))
            .body(propertyMarkerDTO);
    }

    /**
     * {@code PATCH  /property-markers/:id} : Partial updates given fields of an existing propertyMarker, field will ignore if it is null
     *
     * @param id the id of the propertyMarkerDTO to save.
     * @param propertyMarkerDTO the propertyMarkerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyMarkerDTO,
     * or with status {@code 400 (Bad Request)} if the propertyMarkerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the propertyMarkerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the propertyMarkerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PropertyMarkerDTO> partialUpdatePropertyMarker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PropertyMarkerDTO propertyMarkerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PropertyMarker partially : {}, {}", id, propertyMarkerDTO);
        if (propertyMarkerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyMarkerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyMarkerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PropertyMarkerDTO> result = propertyMarkerService.partialUpdate(propertyMarkerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyMarkerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /property-markers} : get all the propertyMarkers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propertyMarkers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PropertyMarkerDTO>> getAllPropertyMarkers(
        PropertyMarkerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PropertyMarkers by criteria: {}", criteria);

        Page<PropertyMarkerDTO> page = propertyMarkerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /property-markers/count} : count all the propertyMarkers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPropertyMarkers(PropertyMarkerCriteria criteria) {
        LOG.debug("REST request to count PropertyMarkers by criteria: {}", criteria);
        return ResponseEntity.ok().body(propertyMarkerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /property-markers/:id} : get the "id" propertyMarker.
     *
     * @param id the id of the propertyMarkerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyMarkerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyMarkerDTO> getPropertyMarker(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PropertyMarker : {}", id);
        Optional<PropertyMarkerDTO> propertyMarkerDTO = propertyMarkerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propertyMarkerDTO);
    }

    /**
     * {@code DELETE  /property-markers/:id} : delete the "id" propertyMarker.
     *
     * @param id the id of the propertyMarkerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropertyMarker(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PropertyMarker : {}", id);
        propertyMarkerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
