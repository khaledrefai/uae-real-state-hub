package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.SubscriptionPlan;
import com.yarmook.realstate.repository.SubscriptionPlanRepository;
import com.yarmook.realstate.service.dto.SubscriptionPlanDTO;
import com.yarmook.realstate.service.mapper.SubscriptionPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.SubscriptionPlan}.
 */
@Service
public class SubscriptionPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPlanService.class);

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final SubscriptionPlanMapper subscriptionPlanMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public SubscriptionPlanService(
        SubscriptionPlanRepository subscriptionPlanRepository,
        SubscriptionPlanMapper subscriptionPlanMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a subscriptionPlan.
     *
     * @param subscriptionPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionPlanDTO save(SubscriptionPlanDTO subscriptionPlanDTO) {
        LOG.debug("Request to save SubscriptionPlan : {}", subscriptionPlanDTO);
        SubscriptionPlan subscriptionPlan = subscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        if (subscriptionPlan.getId() == null) {
            subscriptionPlan.setId(sequenceGeneratorService.generateSequence(SubscriptionPlan.SEQUENCE_NAME));
        }
        subscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return subscriptionPlanMapper.toDto(subscriptionPlan);
    }

    /**
     * Update a subscriptionPlan.
     *
     * @param subscriptionPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionPlanDTO update(SubscriptionPlanDTO subscriptionPlanDTO) {
        LOG.debug("Request to update SubscriptionPlan : {}", subscriptionPlanDTO);
        SubscriptionPlan subscriptionPlan = subscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        subscriptionPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return subscriptionPlanMapper.toDto(subscriptionPlan);
    }

    /**
     * Partially update a subscriptionPlan.
     *
     * @param subscriptionPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionPlanDTO> partialUpdate(SubscriptionPlanDTO subscriptionPlanDTO) {
        LOG.debug("Request to partially update SubscriptionPlan : {}", subscriptionPlanDTO);

        return subscriptionPlanRepository
            .findById(subscriptionPlanDTO.getId())
            .map(existingSubscriptionPlan -> {
                subscriptionPlanMapper.partialUpdate(existingSubscriptionPlan, subscriptionPlanDTO);

                return existingSubscriptionPlan;
            })
            .map(subscriptionPlanRepository::save)
            .map(subscriptionPlanMapper::toDto);
    }

    /**
     * Get one subscriptionPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SubscriptionPlanDTO> findOne(Long id) {
        LOG.debug("Request to get SubscriptionPlan : {}", id);
        return subscriptionPlanRepository.findById(id).map(subscriptionPlanMapper::toDto);
    }

    /**
     * Delete the subscriptionPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SubscriptionPlan : {}", id);
        subscriptionPlanRepository.deleteById(id);
    }
}
