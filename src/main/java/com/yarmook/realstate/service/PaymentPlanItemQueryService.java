package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.PaymentPlanItem;
import com.yarmook.realstate.repository.PaymentPlanItemRepository;
import com.yarmook.realstate.service.criteria.PaymentPlanItemCriteria;
import com.yarmook.realstate.service.dto.PaymentPlanItemDTO;
import com.yarmook.realstate.service.mapper.PaymentPlanItemMapper;
import com.yarmook.realstate.service.util.FilterMatcher;
import com.yarmook.realstate.service.util.MongoPageUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * MongoDB-backed query service for {@link PaymentPlanItem}.
 */
@Service
public class PaymentPlanItemQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentPlanItemQueryService.class);

    private final PaymentPlanItemRepository paymentPlanItemRepository;
    private final PaymentPlanItemMapper paymentPlanItemMapper;

    public PaymentPlanItemQueryService(PaymentPlanItemRepository paymentPlanItemRepository, PaymentPlanItemMapper paymentPlanItemMapper) {
        this.paymentPlanItemRepository = paymentPlanItemRepository;
        this.paymentPlanItemMapper = paymentPlanItemMapper;
    }

    public Page<PaymentPlanItemDTO> findByCriteria(PaymentPlanItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<PaymentPlanItem> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(paymentPlanItemMapper::toDto);
    }

    public long countByCriteria(PaymentPlanItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<PaymentPlanItem> filterByCriteria(PaymentPlanItemCriteria criteria) {
        return paymentPlanItemRepository
            .findAll()
            .stream()
            .filter(entity -> matchesCriteria(entity, criteria))
            .collect(Collectors.toList());
    }

    private boolean matchesCriteria(PaymentPlanItem entity, PaymentPlanItemCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matchesRange(criteria.getOrderNo(), entity.getOrderNo()) &&
            FilterMatcher.matches(criteria.getPaymentTime(), entity.getPaymentTime()) &&
            FilterMatcher.matches(criteria.getPercentOfPayment(), entity.getPercentOfPayment()) &&
            FilterMatcher.matchesRange(criteria.getPlanId(), entity.getPlan() != null ? entity.getPlan().getId() : null)
        );
    }
}
