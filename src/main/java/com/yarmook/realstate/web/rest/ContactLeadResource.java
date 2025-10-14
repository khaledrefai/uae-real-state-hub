package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.repository.ContactLeadRepository;
import com.yarmook.realstate.service.ContactLeadQueryService;
import com.yarmook.realstate.service.ContactLeadService;
import com.yarmook.realstate.service.criteria.ContactLeadCriteria;
import com.yarmook.realstate.service.dto.ContactLeadDTO;
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
 * REST controller for managing {@link com.yarmook.realstate.domain.ContactLead}.
 */
@RestController
@RequestMapping("/api/contact-leads")
public class ContactLeadResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContactLeadResource.class);

    private static final String ENTITY_NAME = "contactLead";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactLeadService contactLeadService;

    private final ContactLeadRepository contactLeadRepository;

    private final ContactLeadQueryService contactLeadQueryService;

    public ContactLeadResource(
        ContactLeadService contactLeadService,
        ContactLeadRepository contactLeadRepository,
        ContactLeadQueryService contactLeadQueryService
    ) {
        this.contactLeadService = contactLeadService;
        this.contactLeadRepository = contactLeadRepository;
        this.contactLeadQueryService = contactLeadQueryService;
    }

    /**
     * {@code POST  /contact-leads} : Create a new contactLead.
     *
     * @param contactLeadDTO the contactLeadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactLeadDTO, or with status {@code 400 (Bad Request)} if the contactLead has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactLeadDTO> createContactLead(@Valid @RequestBody ContactLeadDTO contactLeadDTO) throws URISyntaxException {
        LOG.debug("REST request to save ContactLead : {}", contactLeadDTO);
        if (contactLeadDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactLead cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactLeadDTO = contactLeadService.save(contactLeadDTO);
        return ResponseEntity.created(new URI("/api/contact-leads/" + contactLeadDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contactLeadDTO.getId().toString()))
            .body(contactLeadDTO);
    }

    /**
     * {@code PUT  /contact-leads/:id} : Updates an existing contactLead.
     *
     * @param id the id of the contactLeadDTO to save.
     * @param contactLeadDTO the contactLeadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactLeadDTO,
     * or with status {@code 400 (Bad Request)} if the contactLeadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactLeadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactLeadDTO> updateContactLead(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactLeadDTO contactLeadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContactLead : {}, {}", id, contactLeadDTO);
        if (contactLeadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactLeadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactLeadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactLeadDTO = contactLeadService.update(contactLeadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactLeadDTO.getId().toString()))
            .body(contactLeadDTO);
    }

    /**
     * {@code PATCH  /contact-leads/:id} : Partial updates given fields of an existing contactLead, field will ignore if it is null
     *
     * @param id the id of the contactLeadDTO to save.
     * @param contactLeadDTO the contactLeadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactLeadDTO,
     * or with status {@code 400 (Bad Request)} if the contactLeadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactLeadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactLeadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactLeadDTO> partialUpdateContactLead(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactLeadDTO contactLeadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContactLead partially : {}, {}", id, contactLeadDTO);
        if (contactLeadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactLeadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactLeadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactLeadDTO> result = contactLeadService.partialUpdate(contactLeadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactLeadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-leads} : get all the contactLeads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactLeads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactLeadDTO>> getAllContactLeads(
        ContactLeadCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ContactLeads by criteria: {}", criteria);

        Page<ContactLeadDTO> page = contactLeadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-leads/count} : count all the contactLeads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContactLeads(ContactLeadCriteria criteria) {
        LOG.debug("REST request to count ContactLeads by criteria: {}", criteria);
        return ResponseEntity.ok().body(contactLeadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contact-leads/:id} : get the "id" contactLead.
     *
     * @param id the id of the contactLeadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactLeadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactLeadDTO> getContactLead(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContactLead : {}", id);
        Optional<ContactLeadDTO> contactLeadDTO = contactLeadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactLeadDTO);
    }

    /**
     * {@code DELETE  /contact-leads/:id} : delete the "id" contactLead.
     *
     * @param id the id of the contactLeadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactLead(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContactLead : {}", id);
        contactLeadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
