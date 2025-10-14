package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.BillingSubscription;
import com.yarmook.realstate.repository.BillingSubscriptionRepository;
import com.yarmook.realstate.service.dto.BillingSubscriptionDTO;
import com.yarmook.realstate.service.mapper.BillingSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.BillingSubscription}.
 */
@Service
public class BillingSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingSubscriptionService.class);

    private final BillingSubscriptionRepository billingSubscriptionRepository;

    private final BillingSubscriptionMapper billingSubscriptionMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public BillingSubscriptionService(
        BillingSubscriptionRepository billingSubscriptionRepository,
        BillingSubscriptionMapper billingSubscriptionMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.billingSubscriptionRepository = billingSubscriptionRepository;
        this.billingSubscriptionMapper = billingSubscriptionMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a billingSubscription.
     *
     * @param billingSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public BillingSubscriptionDTO save(BillingSubscriptionDTO billingSubscriptionDTO) {
        LOG.debug("Request to save BillingSubscription : {}", billingSubscriptionDTO);
        BillingSubscription billingSubscription = billingSubscriptionMapper.toEntity(billingSubscriptionDTO);
        if (billingSubscription.getId() == null) {
            billingSubscription.setId(sequenceGeneratorService.generateSequence(BillingSubscription.SEQUENCE_NAME));
        }
        billingSubscription = billingSubscriptionRepository.save(billingSubscription);
        return billingSubscriptionMapper.toDto(billingSubscription);
    }

    /**
     * Update a billingSubscription.
     *
     * @param billingSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public BillingSubscriptionDTO update(BillingSubscriptionDTO billingSubscriptionDTO) {
        LOG.debug("Request to update BillingSubscription : {}", billingSubscriptionDTO);
        BillingSubscription billingSubscription = billingSubscriptionMapper.toEntity(billingSubscriptionDTO);
        billingSubscription = billingSubscriptionRepository.save(billingSubscription);
        return billingSubscriptionMapper.toDto(billingSubscription);
    }

    /**
     * Partially update a billingSubscription.
     *
     * @param billingSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BillingSubscriptionDTO> partialUpdate(BillingSubscriptionDTO billingSubscriptionDTO) {
        LOG.debug("Request to partially update BillingSubscription : {}", billingSubscriptionDTO);

        return billingSubscriptionRepository
            .findById(billingSubscriptionDTO.getId())
            .map(existingBillingSubscription -> {
                billingSubscriptionMapper.partialUpdate(existingBillingSubscription, billingSubscriptionDTO);

                return existingBillingSubscription;
            })
            .map(billingSubscriptionRepository::save)
            .map(billingSubscriptionMapper::toDto);
    }

    /**
     * Get one billingSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<BillingSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get BillingSubscription : {}", id);
        return billingSubscriptionRepository.findById(id).map(billingSubscriptionMapper::toDto);
    }

    /**
     * Delete the billingSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BillingSubscription : {}", id);
        billingSubscriptionRepository.deleteById(id);
    }
}
