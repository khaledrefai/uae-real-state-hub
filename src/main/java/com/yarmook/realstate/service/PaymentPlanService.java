package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.PaymentPlan;
import com.yarmook.realstate.repository.PaymentPlanRepository;
import com.yarmook.realstate.service.dto.PaymentPlanDTO;
import com.yarmook.realstate.service.mapper.PaymentPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.PaymentPlan}.
 */
@Service
public class PaymentPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentPlanService.class);

    private final PaymentPlanRepository paymentPlanRepository;

    private final PaymentPlanMapper paymentPlanMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public PaymentPlanService(
        PaymentPlanRepository paymentPlanRepository,
        PaymentPlanMapper paymentPlanMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanMapper = paymentPlanMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a paymentPlan.
     *
     * @param paymentPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentPlanDTO save(PaymentPlanDTO paymentPlanDTO) {
        LOG.debug("Request to save PaymentPlan : {}", paymentPlanDTO);
        PaymentPlan paymentPlan = paymentPlanMapper.toEntity(paymentPlanDTO);
        if (paymentPlan.getId() == null) {
            paymentPlan.setId(sequenceGeneratorService.generateSequence(PaymentPlan.SEQUENCE_NAME));
        }
        paymentPlan = paymentPlanRepository.save(paymentPlan);
        return paymentPlanMapper.toDto(paymentPlan);
    }

    /**
     * Update a paymentPlan.
     *
     * @param paymentPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentPlanDTO update(PaymentPlanDTO paymentPlanDTO) {
        LOG.debug("Request to update PaymentPlan : {}", paymentPlanDTO);
        PaymentPlan paymentPlan = paymentPlanMapper.toEntity(paymentPlanDTO);
        paymentPlan = paymentPlanRepository.save(paymentPlan);
        return paymentPlanMapper.toDto(paymentPlan);
    }

    /**
     * Partially update a paymentPlan.
     *
     * @param paymentPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentPlanDTO> partialUpdate(PaymentPlanDTO paymentPlanDTO) {
        LOG.debug("Request to partially update PaymentPlan : {}", paymentPlanDTO);

        return paymentPlanRepository
            .findById(paymentPlanDTO.getId())
            .map(existingPaymentPlan -> {
                paymentPlanMapper.partialUpdate(existingPaymentPlan, paymentPlanDTO);

                return existingPaymentPlan;
            })
            .map(paymentPlanRepository::save)
            .map(paymentPlanMapper::toDto);
    }

    /**
     * Get one paymentPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PaymentPlanDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentPlan : {}", id);
        return paymentPlanRepository.findById(id).map(paymentPlanMapper::toDto);
    }

    /**
     * Delete the paymentPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentPlan : {}", id);
        paymentPlanRepository.deleteById(id);
    }
}
