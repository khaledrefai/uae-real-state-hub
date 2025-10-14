package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.SubscriptionPlanRepository;
import com.yarmook.realstate.service.SubscriptionPlanQueryService;
import com.yarmook.realstate.service.SubscriptionPlanService;
import com.yarmook.realstate.service.criteria.SubscriptionPlanCriteria;
import com.yarmook.realstate.service.dto.SubscriptionPlanDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.SubscriptionPlan}.
 */
@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPlanResource.class);

    private static final String ENTITY_NAME = "subscriptionPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionPlanService subscriptionPlanService;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final SubscriptionPlanQueryService subscriptionPlanQueryService;

    public SubscriptionPlanResource(
        SubscriptionPlanService subscriptionPlanService,
        SubscriptionPlanRepository subscriptionPlanRepository,
        SubscriptionPlanQueryService subscriptionPlanQueryService
    ) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanQueryService = subscriptionPlanQueryService;
    }

    /**
     * {@code POST  /subscription-plans} : Create a new subscriptionPlan.
     *
     * @param subscriptionPlanDTO the subscriptionPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionPlanDTO, or with status {@code 400 (Bad Request)} if the subscriptionPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SubscriptionPlan : {}", subscriptionPlanDTO);
        if (subscriptionPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subscriptionPlanDTO = subscriptionPlanService.save(subscriptionPlanDTO);
        return ResponseEntity.created(new URI("/api/subscription-plans/" + subscriptionPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subscriptionPlanDTO.getId().toString()))
            .body(subscriptionPlanDTO);
    }

    /**
     * {@code PUT  /subscription-plans/:id} : Updates an existing subscriptionPlan.
     *
     * @param id the id of the subscriptionPlanDTO to save.
     * @param subscriptionPlanDTO the subscriptionPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionPlanDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubscriptionPlan : {}, {}", id, subscriptionPlanDTO);
        if (subscriptionPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subscriptionPlanDTO = subscriptionPlanService.update(subscriptionPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionPlanDTO.getId().toString()))
            .body(subscriptionPlanDTO);
    }

    /**
     * {@code PATCH  /subscription-plans/:id} : Partial updates given fields of an existing subscriptionPlan, field will ignore if it is null
     *
     * @param id the id of the subscriptionPlanDTO to save.
     * @param subscriptionPlanDTO the subscriptionPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionPlanDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionPlanDTO> partialUpdateSubscriptionPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubscriptionPlan partially : {}, {}", id, subscriptionPlanDTO);
        if (subscriptionPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionPlanDTO> result = subscriptionPlanService.partialUpdate(subscriptionPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-plans} : get all the subscriptionPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionPlans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlans(
        SubscriptionPlanCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SubscriptionPlans by criteria: {}", criteria);

        Page<SubscriptionPlanDTO> page = subscriptionPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-plans/count} : count all the subscriptionPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubscriptionPlans(SubscriptionPlanCriteria criteria) {
        LOG.debug("REST request to count SubscriptionPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(subscriptionPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subscription-plans/:id} : get the "id" subscriptionPlan.
     *
     * @param id the id of the subscriptionPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubscriptionPlan : {}", id);
        Optional<SubscriptionPlanDTO> subscriptionPlanDTO = subscriptionPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionPlanDTO);
    }

    /**
     * {@code DELETE  /subscription-plans/:id} : delete the "id" subscriptionPlan.
     *
     * @param id the id of the subscriptionPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubscriptionPlan : {}", id);
        subscriptionPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
