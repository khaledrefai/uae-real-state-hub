package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.PaymentPlanItem;
import com.yarmook.realstate.repository.PaymentPlanItemRepository;
import com.yarmook.realstate.service.dto.PaymentPlanItemDTO;
import com.yarmook.realstate.service.mapper.PaymentPlanItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.PaymentPlanItem}.
 */
@Service
public class PaymentPlanItemService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentPlanItemService.class);

    private final PaymentPlanItemRepository paymentPlanItemRepository;

    private final PaymentPlanItemMapper paymentPlanItemMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public PaymentPlanItemService(
        PaymentPlanItemRepository paymentPlanItemRepository,
        PaymentPlanItemMapper paymentPlanItemMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.paymentPlanItemRepository = paymentPlanItemRepository;
        this.paymentPlanItemMapper = paymentPlanItemMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a paymentPlanItem.
     *
     * @param paymentPlanItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentPlanItemDTO save(PaymentPlanItemDTO paymentPlanItemDTO) {
        LOG.debug("Request to save PaymentPlanItem : {}", paymentPlanItemDTO);
        PaymentPlanItem paymentPlanItem = paymentPlanItemMapper.toEntity(paymentPlanItemDTO);
        if (paymentPlanItem.getId() == null) {
            paymentPlanItem.setId(sequenceGeneratorService.generateSequence(PaymentPlanItem.SEQUENCE_NAME));
        }
        paymentPlanItem = paymentPlanItemRepository.save(paymentPlanItem);
        return paymentPlanItemMapper.toDto(paymentPlanItem);
    }

    /**
     * Update a paymentPlanItem.
     *
     * @param paymentPlanItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentPlanItemDTO update(PaymentPlanItemDTO paymentPlanItemDTO) {
        LOG.debug("Request to update PaymentPlanItem : {}", paymentPlanItemDTO);
        PaymentPlanItem paymentPlanItem = paymentPlanItemMapper.toEntity(paymentPlanItemDTO);
        paymentPlanItem = paymentPlanItemRepository.save(paymentPlanItem);
        return paymentPlanItemMapper.toDto(paymentPlanItem);
    }

    /**
     * Partially update a paymentPlanItem.
     *
     * @param paymentPlanItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentPlanItemDTO> partialUpdate(PaymentPlanItemDTO paymentPlanItemDTO) {
        LOG.debug("Request to partially update PaymentPlanItem : {}", paymentPlanItemDTO);

        return paymentPlanItemRepository
            .findById(paymentPlanItemDTO.getId())
            .map(existingPaymentPlanItem -> {
                paymentPlanItemMapper.partialUpdate(existingPaymentPlanItem, paymentPlanItemDTO);

                return existingPaymentPlanItem;
            })
            .map(paymentPlanItemRepository::save)
            .map(paymentPlanItemMapper::toDto);
    }

    /**
     * Get one paymentPlanItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PaymentPlanItemDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentPlanItem : {}", id);
        return paymentPlanItemRepository.findById(id).map(paymentPlanItemMapper::toDto);
    }

    /**
     * Delete the paymentPlanItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentPlanItem : {}", id);
        paymentPlanItemRepository.deleteById(id);
    }
}
