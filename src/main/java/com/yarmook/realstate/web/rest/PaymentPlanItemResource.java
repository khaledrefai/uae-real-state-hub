package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.PaymentPlanItemRepository;
import com.yarmook.realstate.service.PaymentPlanItemQueryService;
import com.yarmook.realstate.service.PaymentPlanItemService;
import com.yarmook.realstate.service.criteria.PaymentPlanItemCriteria;
import com.yarmook.realstate.service.dto.PaymentPlanItemDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.PaymentPlanItem}.
 */
@RestController
@RequestMapping("/api/payment-plan-items")
public class PaymentPlanItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentPlanItemResource.class);

    private static final String ENTITY_NAME = "paymentPlanItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentPlanItemService paymentPlanItemService;

    private final PaymentPlanItemRepository paymentPlanItemRepository;

    private final PaymentPlanItemQueryService paymentPlanItemQueryService;

    public PaymentPlanItemResource(
        PaymentPlanItemService paymentPlanItemService,
        PaymentPlanItemRepository paymentPlanItemRepository,
        PaymentPlanItemQueryService paymentPlanItemQueryService
    ) {
        this.paymentPlanItemService = paymentPlanItemService;
        this.paymentPlanItemRepository = paymentPlanItemRepository;
        this.paymentPlanItemQueryService = paymentPlanItemQueryService;
    }

    /**
     * {@code POST  /payment-plan-items} : Create a new paymentPlanItem.
     *
     * @param paymentPlanItemDTO the paymentPlanItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentPlanItemDTO, or with status {@code 400 (Bad Request)} if the paymentPlanItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentPlanItemDTO> createPaymentPlanItem(@Valid @RequestBody PaymentPlanItemDTO paymentPlanItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentPlanItem : {}", paymentPlanItemDTO);
        if (paymentPlanItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentPlanItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentPlanItemDTO = paymentPlanItemService.save(paymentPlanItemDTO);
        return ResponseEntity.created(new URI("/api/payment-plan-items/" + paymentPlanItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentPlanItemDTO.getId().toString()))
            .body(paymentPlanItemDTO);
    }

    /**
     * {@code PUT  /payment-plan-items/:id} : Updates an existing paymentPlanItem.
     *
     * @param id the id of the paymentPlanItemDTO to save.
     * @param paymentPlanItemDTO the paymentPlanItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentPlanItemDTO,
     * or with status {@code 400 (Bad Request)} if the paymentPlanItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentPlanItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentPlanItemDTO> updatePaymentPlanItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentPlanItemDTO paymentPlanItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentPlanItem : {}, {}", id, paymentPlanItemDTO);
        if (paymentPlanItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentPlanItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentPlanItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentPlanItemDTO = paymentPlanItemService.update(paymentPlanItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentPlanItemDTO.getId().toString()))
            .body(paymentPlanItemDTO);
    }

    /**
     * {@code PATCH  /payment-plan-items/:id} : Partial updates given fields of an existing paymentPlanItem, field will ignore if it is null
     *
     * @param id the id of the paymentPlanItemDTO to save.
     * @param paymentPlanItemDTO the paymentPlanItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentPlanItemDTO,
     * or with status {@code 400 (Bad Request)} if the paymentPlanItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentPlanItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentPlanItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentPlanItemDTO> partialUpdatePaymentPlanItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentPlanItemDTO paymentPlanItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentPlanItem partially : {}, {}", id, paymentPlanItemDTO);
        if (paymentPlanItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentPlanItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentPlanItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentPlanItemDTO> result = paymentPlanItemService.partialUpdate(paymentPlanItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentPlanItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-plan-items} : get all the paymentPlanItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentPlanItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentPlanItemDTO>> getAllPaymentPlanItems(
        PaymentPlanItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentPlanItems by criteria: {}", criteria);

        Page<PaymentPlanItemDTO> page = paymentPlanItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-plan-items/count} : count all the paymentPlanItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentPlanItems(PaymentPlanItemCriteria criteria) {
        LOG.debug("REST request to count PaymentPlanItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentPlanItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-plan-items/:id} : get the "id" paymentPlanItem.
     *
     * @param id the id of the paymentPlanItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentPlanItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentPlanItemDTO> getPaymentPlanItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentPlanItem : {}", id);
        Optional<PaymentPlanItemDTO> paymentPlanItemDTO = paymentPlanItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentPlanItemDTO);
    }

    /**
     * {@code DELETE  /payment-plan-items/:id} : delete the "id" paymentPlanItem.
     *
     * @param id the id of the paymentPlanItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentPlanItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentPlanItem : {}", id);
        paymentPlanItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
