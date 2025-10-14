package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.PaymentPlanRepository;
import com.yarmook.realstate.service.PaymentPlanQueryService;
import com.yarmook.realstate.service.PaymentPlanService;
import com.yarmook.realstate.service.criteria.PaymentPlanCriteria;
import com.yarmook.realstate.service.dto.PaymentPlanDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.PaymentPlan}.
 */
@RestController
@RequestMapping("/api/payment-plans")
public class PaymentPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentPlanResource.class);

    private static final String ENTITY_NAME = "paymentPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentPlanService paymentPlanService;

    private final PaymentPlanRepository paymentPlanRepository;

    private final PaymentPlanQueryService paymentPlanQueryService;

    public PaymentPlanResource(
        PaymentPlanService paymentPlanService,
        PaymentPlanRepository paymentPlanRepository,
        PaymentPlanQueryService paymentPlanQueryService
    ) {
        this.paymentPlanService = paymentPlanService;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanQueryService = paymentPlanQueryService;
    }

    /**
     * {@code POST  /payment-plans} : Create a new paymentPlan.
     *
     * @param paymentPlanDTO the paymentPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentPlanDTO, or with status {@code 400 (Bad Request)} if the paymentPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentPlanDTO> createPaymentPlan(@Valid @RequestBody PaymentPlanDTO paymentPlanDTO) throws URISyntaxException {
        LOG.debug("REST request to save PaymentPlan : {}", paymentPlanDTO);
        if (paymentPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentPlanDTO = paymentPlanService.save(paymentPlanDTO);
        return ResponseEntity.created(new URI("/api/payment-plans/" + paymentPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentPlanDTO.getId().toString()))
            .body(paymentPlanDTO);
    }

    /**
     * {@code PUT  /payment-plans/:id} : Updates an existing paymentPlan.
     *
     * @param id the id of the paymentPlanDTO to save.
     * @param paymentPlanDTO the paymentPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentPlanDTO,
     * or with status {@code 400 (Bad Request)} if the paymentPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentPlanDTO> updatePaymentPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentPlanDTO paymentPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentPlan : {}, {}", id, paymentPlanDTO);
        if (paymentPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentPlanDTO = paymentPlanService.update(paymentPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentPlanDTO.getId().toString()))
            .body(paymentPlanDTO);
    }

    /**
     * {@code PATCH  /payment-plans/:id} : Partial updates given fields of an existing paymentPlan, field will ignore if it is null
     *
     * @param id the id of the paymentPlanDTO to save.
     * @param paymentPlanDTO the paymentPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentPlanDTO,
     * or with status {@code 400 (Bad Request)} if the paymentPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentPlanDTO> partialUpdatePaymentPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentPlanDTO paymentPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentPlan partially : {}, {}", id, paymentPlanDTO);
        if (paymentPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentPlanDTO> result = paymentPlanService.partialUpdate(paymentPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-plans} : get all the paymentPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentPlans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentPlanDTO>> getAllPaymentPlans(
        PaymentPlanCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentPlans by criteria: {}", criteria);

        Page<PaymentPlanDTO> page = paymentPlanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-plans/count} : count all the paymentPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentPlans(PaymentPlanCriteria criteria) {
        LOG.debug("REST request to count PaymentPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentPlanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-plans/:id} : get the "id" paymentPlan.
     *
     * @param id the id of the paymentPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentPlanDTO> getPaymentPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentPlan : {}", id);
        Optional<PaymentPlanDTO> paymentPlanDTO = paymentPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentPlanDTO);
    }

    /**
     * {@code DELETE  /payment-plans/:id} : delete the "id" paymentPlan.
     *
     * @param id the id of the paymentPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentPlan : {}", id);
        paymentPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
