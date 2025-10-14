package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.BillingSubscriptionRepository;
import com.yarmook.realstate.service.BillingSubscriptionQueryService;
import com.yarmook.realstate.service.BillingSubscriptionService;
import com.yarmook.realstate.service.criteria.BillingSubscriptionCriteria;
import com.yarmook.realstate.service.dto.BillingSubscriptionDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.BillingSubscription}.
 */
@RestController
@RequestMapping("/api/billing-subscriptions")
public class BillingSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillingSubscriptionResource.class);

    private static final String ENTITY_NAME = "billingSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillingSubscriptionService billingSubscriptionService;

    private final BillingSubscriptionRepository billingSubscriptionRepository;

    private final BillingSubscriptionQueryService billingSubscriptionQueryService;

    public BillingSubscriptionResource(
        BillingSubscriptionService billingSubscriptionService,
        BillingSubscriptionRepository billingSubscriptionRepository,
        BillingSubscriptionQueryService billingSubscriptionQueryService
    ) {
        this.billingSubscriptionService = billingSubscriptionService;
        this.billingSubscriptionRepository = billingSubscriptionRepository;
        this.billingSubscriptionQueryService = billingSubscriptionQueryService;
    }

    /**
     * {@code POST  /billing-subscriptions} : Create a new billingSubscription.
     *
     * @param billingSubscriptionDTO the billingSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingSubscriptionDTO, or with status {@code 400 (Bad Request)} if the billingSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillingSubscriptionDTO> createBillingSubscription(
        @Valid @RequestBody BillingSubscriptionDTO billingSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save BillingSubscription : {}", billingSubscriptionDTO);
        if (billingSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new billingSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billingSubscriptionDTO = billingSubscriptionService.save(billingSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/billing-subscriptions/" + billingSubscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billingSubscriptionDTO.getId().toString()))
            .body(billingSubscriptionDTO);
    }

    /**
     * {@code PUT  /billing-subscriptions/:id} : Updates an existing billingSubscription.
     *
     * @param id the id of the billingSubscriptionDTO to save.
     * @param billingSubscriptionDTO the billingSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the billingSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillingSubscriptionDTO> updateBillingSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillingSubscriptionDTO billingSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BillingSubscription : {}, {}", id, billingSubscriptionDTO);
        if (billingSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billingSubscriptionDTO = billingSubscriptionService.update(billingSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingSubscriptionDTO.getId().toString()))
            .body(billingSubscriptionDTO);
    }

    /**
     * {@code PATCH  /billing-subscriptions/:id} : Partial updates given fields of an existing billingSubscription, field will ignore if it is null
     *
     * @param id the id of the billingSubscriptionDTO to save.
     * @param billingSubscriptionDTO the billingSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the billingSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the billingSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the billingSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillingSubscriptionDTO> partialUpdateBillingSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillingSubscriptionDTO billingSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BillingSubscription partially : {}, {}", id, billingSubscriptionDTO);
        if (billingSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillingSubscriptionDTO> result = billingSubscriptionService.partialUpdate(billingSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billingSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /billing-subscriptions} : get all the billingSubscriptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billingSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BillingSubscriptionDTO>> getAllBillingSubscriptions(
        BillingSubscriptionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BillingSubscriptions by criteria: {}", criteria);

        Page<BillingSubscriptionDTO> page = billingSubscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /billing-subscriptions/count} : count all the billingSubscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBillingSubscriptions(BillingSubscriptionCriteria criteria) {
        LOG.debug("REST request to count BillingSubscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(billingSubscriptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /billing-subscriptions/:id} : get the "id" billingSubscription.
     *
     * @param id the id of the billingSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billingSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillingSubscriptionDTO> getBillingSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BillingSubscription : {}", id);
        Optional<BillingSubscriptionDTO> billingSubscriptionDTO = billingSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billingSubscriptionDTO);
    }

    /**
     * {@code DELETE  /billing-subscriptions/:id} : delete the "id" billingSubscription.
     *
     * @param id the id of the billingSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillingSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BillingSubscription : {}", id);
        billingSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
