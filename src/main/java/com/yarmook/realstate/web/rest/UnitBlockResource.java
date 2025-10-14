package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.UnitBlockRepository;
import com.yarmook.realstate.service.UnitBlockQueryService;
import com.yarmook.realstate.service.UnitBlockService;
import com.yarmook.realstate.service.criteria.UnitBlockCriteria;
import com.yarmook.realstate.service.dto.UnitBlockDTO;
import com.yarmook.realstate.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.UnitBlock}.
 */
@RestController
@RequestMapping("/api/unit-blocks")
public class UnitBlockResource {

    private static final Logger LOG = LoggerFactory.getLogger(UnitBlockResource.class);

    private static final String ENTITY_NAME = "unitBlock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitBlockService unitBlockService;

    private final UnitBlockRepository unitBlockRepository;

    private final UnitBlockQueryService unitBlockQueryService;

    public UnitBlockResource(
        UnitBlockService unitBlockService,
        UnitBlockRepository unitBlockRepository,
        UnitBlockQueryService unitBlockQueryService
    ) {
        this.unitBlockService = unitBlockService;
        this.unitBlockRepository = unitBlockRepository;
        this.unitBlockQueryService = unitBlockQueryService;
    }

    /**
     * {@code POST  /unit-blocks} : Create a new unitBlock.
     *
     * @param unitBlockDTO the unitBlockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitBlockDTO, or with status {@code 400 (Bad Request)} if the unitBlock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UnitBlockDTO> createUnitBlock(@RequestBody UnitBlockDTO unitBlockDTO) throws URISyntaxException {
        LOG.debug("REST request to save UnitBlock : {}", unitBlockDTO);
        if (unitBlockDTO.getId() != null) {
            throw new BadRequestAlertException("A new unitBlock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        unitBlockDTO = unitBlockService.save(unitBlockDTO);
        return ResponseEntity.created(new URI("/api/unit-blocks/" + unitBlockDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, unitBlockDTO.getId().toString()))
            .body(unitBlockDTO);
    }

    /**
     * {@code PUT  /unit-blocks/:id} : Updates an existing unitBlock.
     *
     * @param id the id of the unitBlockDTO to save.
     * @param unitBlockDTO the unitBlockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitBlockDTO,
     * or with status {@code 400 (Bad Request)} if the unitBlockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitBlockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnitBlockDTO> updateUnitBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UnitBlockDTO unitBlockDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UnitBlock : {}, {}", id, unitBlockDTO);
        if (unitBlockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitBlockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        unitBlockDTO = unitBlockService.update(unitBlockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitBlockDTO.getId().toString()))
            .body(unitBlockDTO);
    }

    /**
     * {@code PATCH  /unit-blocks/:id} : Partial updates given fields of an existing unitBlock, field will ignore if it is null
     *
     * @param id the id of the unitBlockDTO to save.
     * @param unitBlockDTO the unitBlockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitBlockDTO,
     * or with status {@code 400 (Bad Request)} if the unitBlockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitBlockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitBlockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitBlockDTO> partialUpdateUnitBlock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UnitBlockDTO unitBlockDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UnitBlock partially : {}, {}", id, unitBlockDTO);
        if (unitBlockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitBlockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitBlockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitBlockDTO> result = unitBlockService.partialUpdate(unitBlockDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitBlockDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-blocks} : get all the unitBlocks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitBlocks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UnitBlockDTO>> getAllUnitBlocks(
        UnitBlockCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get UnitBlocks by criteria: {}", criteria);

        Page<UnitBlockDTO> page = unitBlockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-blocks/count} : count all the unitBlocks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUnitBlocks(UnitBlockCriteria criteria) {
        LOG.debug("REST request to count UnitBlocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(unitBlockQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /unit-blocks/:id} : get the "id" unitBlock.
     *
     * @param id the id of the unitBlockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitBlockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitBlockDTO> getUnitBlock(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UnitBlock : {}", id);
        Optional<UnitBlockDTO> unitBlockDTO = unitBlockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitBlockDTO);
    }

    /**
     * {@code DELETE  /unit-blocks/:id} : delete the "id" unitBlock.
     *
     * @param id the id of the unitBlockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitBlock(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UnitBlock : {}", id);
        unitBlockService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
