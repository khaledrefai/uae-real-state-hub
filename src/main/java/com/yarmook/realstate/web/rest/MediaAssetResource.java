package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.MediaAssetRepository;
import com.yarmook.realstate.service.MediaAssetQueryService;
import com.yarmook.realstate.service.MediaAssetService;
import com.yarmook.realstate.service.criteria.MediaAssetCriteria;
import com.yarmook.realstate.service.dto.MediaAssetDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.MediaAsset}.
 */
@RestController
@RequestMapping("/api/media-assets")
public class MediaAssetResource {

    private static final Logger LOG = LoggerFactory.getLogger(MediaAssetResource.class);

    private static final String ENTITY_NAME = "mediaAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MediaAssetService mediaAssetService;

    private final MediaAssetRepository mediaAssetRepository;

    private final MediaAssetQueryService mediaAssetQueryService;

    public MediaAssetResource(
        MediaAssetService mediaAssetService,
        MediaAssetRepository mediaAssetRepository,
        MediaAssetQueryService mediaAssetQueryService
    ) {
        this.mediaAssetService = mediaAssetService;
        this.mediaAssetRepository = mediaAssetRepository;
        this.mediaAssetQueryService = mediaAssetQueryService;
    }

    /**
     * {@code POST  /media-assets} : Create a new mediaAsset.
     *
     * @param mediaAssetDTO the mediaAssetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mediaAssetDTO, or with status {@code 400 (Bad Request)} if the mediaAsset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MediaAssetDTO> createMediaAsset(@Valid @RequestBody MediaAssetDTO mediaAssetDTO) throws URISyntaxException {
        LOG.debug("REST request to save MediaAsset : {}", mediaAssetDTO);
        if (mediaAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new mediaAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mediaAssetDTO = mediaAssetService.save(mediaAssetDTO);
        return ResponseEntity.created(new URI("/api/media-assets/" + mediaAssetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mediaAssetDTO.getId().toString()))
            .body(mediaAssetDTO);
    }

    /**
     * {@code PUT  /media-assets/:id} : Updates an existing mediaAsset.
     *
     * @param id the id of the mediaAssetDTO to save.
     * @param mediaAssetDTO the mediaAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mediaAssetDTO,
     * or with status {@code 400 (Bad Request)} if the mediaAssetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mediaAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MediaAssetDTO> updateMediaAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MediaAssetDTO mediaAssetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MediaAsset : {}, {}", id, mediaAssetDTO);
        if (mediaAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mediaAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mediaAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mediaAssetDTO = mediaAssetService.update(mediaAssetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mediaAssetDTO.getId().toString()))
            .body(mediaAssetDTO);
    }

    /**
     * {@code PATCH  /media-assets/:id} : Partial updates given fields of an existing mediaAsset, field will ignore if it is null
     *
     * @param id the id of the mediaAssetDTO to save.
     * @param mediaAssetDTO the mediaAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mediaAssetDTO,
     * or with status {@code 400 (Bad Request)} if the mediaAssetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mediaAssetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mediaAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MediaAssetDTO> partialUpdateMediaAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MediaAssetDTO mediaAssetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MediaAsset partially : {}, {}", id, mediaAssetDTO);
        if (mediaAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mediaAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mediaAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MediaAssetDTO> result = mediaAssetService.partialUpdate(mediaAssetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mediaAssetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /media-assets} : get all the mediaAssets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mediaAssets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MediaAssetDTO>> getAllMediaAssets(
        MediaAssetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MediaAssets by criteria: {}", criteria);

        Page<MediaAssetDTO> page = mediaAssetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /media-assets/count} : count all the mediaAssets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMediaAssets(MediaAssetCriteria criteria) {
        LOG.debug("REST request to count MediaAssets by criteria: {}", criteria);
        return ResponseEntity.ok().body(mediaAssetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /media-assets/:id} : get the "id" mediaAsset.
     *
     * @param id the id of the mediaAssetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mediaAssetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MediaAssetDTO> getMediaAsset(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MediaAsset : {}", id);
        Optional<MediaAssetDTO> mediaAssetDTO = mediaAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mediaAssetDTO);
    }

    /**
     * {@code DELETE  /media-assets/:id} : delete the "id" mediaAsset.
     *
     * @param id the id of the mediaAssetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaAsset(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MediaAsset : {}", id);
        mediaAssetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
