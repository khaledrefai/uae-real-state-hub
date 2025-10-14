package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.ContactLead;
import com.yarmook.realstate.repository.ContactLeadRepository;
import com.yarmook.realstate.service.dto.ContactLeadDTO;
import com.yarmook.realstate.service.mapper.ContactLeadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.ContactLead}.
 */
@Service
public class ContactLeadService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactLeadService.class);

    private final ContactLeadRepository contactLeadRepository;

    private final ContactLeadMapper contactLeadMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public ContactLeadService(
        ContactLeadRepository contactLeadRepository,
        ContactLeadMapper contactLeadMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.contactLeadRepository = contactLeadRepository;
        this.contactLeadMapper = contactLeadMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a contactLead.
     *
     * @param contactLeadDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactLeadDTO save(ContactLeadDTO contactLeadDTO) {
        LOG.debug("Request to save ContactLead : {}", contactLeadDTO);
        ContactLead contactLead = contactLeadMapper.toEntity(contactLeadDTO);
        if (contactLead.getId() == null) {
            contactLead.setId(sequenceGeneratorService.generateSequence(ContactLead.SEQUENCE_NAME));
        }
        contactLead = contactLeadRepository.save(contactLead);
        return contactLeadMapper.toDto(contactLead);
    }

    /**
     * Update a contactLead.
     *
     * @param contactLeadDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactLeadDTO update(ContactLeadDTO contactLeadDTO) {
        LOG.debug("Request to update ContactLead : {}", contactLeadDTO);
        ContactLead contactLead = contactLeadMapper.toEntity(contactLeadDTO);
        contactLead = contactLeadRepository.save(contactLead);
        return contactLeadMapper.toDto(contactLead);
    }

    /**
     * Partially update a contactLead.
     *
     * @param contactLeadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactLeadDTO> partialUpdate(ContactLeadDTO contactLeadDTO) {
        LOG.debug("Request to partially update ContactLead : {}", contactLeadDTO);

        return contactLeadRepository
            .findById(contactLeadDTO.getId())
            .map(existingContactLead -> {
                contactLeadMapper.partialUpdate(existingContactLead, contactLeadDTO);

                return existingContactLead;
            })
            .map(contactLeadRepository::save)
            .map(contactLeadMapper::toDto);
    }

    /**
     * Get one contactLead by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ContactLeadDTO> findOne(Long id) {
        LOG.debug("Request to get ContactLead : {}", id);
        return contactLeadRepository.findById(id).map(contactLeadMapper::toDto);
    }

    /**
     * Delete the contactLead by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContactLead : {}", id);
        contactLeadRepository.deleteById(id);
    }
}
