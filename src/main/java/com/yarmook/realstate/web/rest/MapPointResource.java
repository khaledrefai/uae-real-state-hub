package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.MapPointRepository;
import com.yarmook.realstate.service.MapPointQueryService;
import com.yarmook.realstate.service.MapPointService;
import com.yarmook.realstate.service.criteria.MapPointCriteria;
import com.yarmook.realstate.service.dto.MapPointDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.MapPoint}.
 */
@RestController
@RequestMapping("/api/map-points")
public class MapPointResource {

    private static final Logger LOG = LoggerFactory.getLogger(MapPointResource.class);

    private static final String ENTITY_NAME = "mapPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MapPointService mapPointService;

    private final MapPointRepository mapPointRepository;

    private final MapPointQueryService mapPointQueryService;

    public MapPointResource(
        MapPointService mapPointService,
        MapPointRepository mapPointRepository,
        MapPointQueryService mapPointQueryService
    ) {
        this.mapPointService = mapPointService;
        this.mapPointRepository = mapPointRepository;
        this.mapPointQueryService = mapPointQueryService;
    }

    /**
     * {@code POST  /map-points} : Create a new mapPoint.
     *
     * @param mapPointDTO the mapPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mapPointDTO, or with status {@code 400 (Bad Request)} if the mapPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MapPointDTO> createMapPoint(@Valid @RequestBody MapPointDTO mapPointDTO) throws URISyntaxException {
        LOG.debug("REST request to save MapPoint : {}", mapPointDTO);
        if (mapPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new mapPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mapPointDTO = mapPointService.save(mapPointDTO);
        return ResponseEntity.created(new URI("/api/map-points/" + mapPointDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mapPointDTO.getId().toString()))
            .body(mapPointDTO);
    }

    /**
     * {@code PUT  /map-points/:id} : Updates an existing mapPoint.
     *
     * @param id the id of the mapPointDTO to save.
     * @param mapPointDTO the mapPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapPointDTO,
     * or with status {@code 400 (Bad Request)} if the mapPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mapPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MapPointDTO> updateMapPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MapPointDTO mapPointDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MapPoint : {}, {}", id, mapPointDTO);
        if (mapPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mapPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mapPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mapPointDTO = mapPointService.update(mapPointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mapPointDTO.getId().toString()))
            .body(mapPointDTO);
    }

    /**
     * {@code PATCH  /map-points/:id} : Partial updates given fields of an existing mapPoint, field will ignore if it is null
     *
     * @param id the id of the mapPointDTO to save.
     * @param mapPointDTO the mapPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mapPointDTO,
     * or with status {@code 400 (Bad Request)} if the mapPointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mapPointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mapPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MapPointDTO> partialUpdateMapPoint(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MapPointDTO mapPointDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MapPoint partially : {}, {}", id, mapPointDTO);
        if (mapPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mapPointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mapPointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MapPointDTO> result = mapPointService.partialUpdate(mapPointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mapPointDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /map-points} : get all the mapPoints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mapPoints in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MapPointDTO>> getAllMapPoints(
        MapPointCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MapPoints by criteria: {}", criteria);

        Page<MapPointDTO> page = mapPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /map-points/count} : count all the mapPoints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMapPoints(MapPointCriteria criteria) {
        LOG.debug("REST request to count MapPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(mapPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /map-points/:id} : get the "id" mapPoint.
     *
     * @param id the id of the mapPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mapPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MapPointDTO> getMapPoint(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MapPoint : {}", id);
        Optional<MapPointDTO> mapPointDTO = mapPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mapPointDTO);
    }

    /**
     * {@code DELETE  /map-points/:id} : delete the "id" mapPoint.
     *
     * @param id the id of the mapPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapPoint(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MapPoint : {}", id);
        mapPointService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
