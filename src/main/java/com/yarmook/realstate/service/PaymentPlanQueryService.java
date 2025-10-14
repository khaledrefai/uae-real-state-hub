package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.PaymentPlan;
import com.yarmook.realstate.repository.PaymentPlanRepository;
import com.yarmook.realstate.service.criteria.PaymentPlanCriteria;
import com.yarmook.realstate.service.dto.PaymentPlanDTO;
import com.yarmook.realstate.service.mapper.PaymentPlanMapper;
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
 * MongoDB-backed query service for {@link PaymentPlan}.
 */
@Service
public class PaymentPlanQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentPlanQueryService.class);

    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentPlanMapper paymentPlanMapper;

    public PaymentPlanQueryService(PaymentPlanRepository paymentPlanRepository, PaymentPlanMapper paymentPlanMapper) {
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentPlanMapper = paymentPlanMapper;
    }

    public Page<PaymentPlanDTO> findByCriteria(PaymentPlanCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<PaymentPlan> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(paymentPlanMapper::toDto);
    }

    public long countByCriteria(PaymentPlanCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<PaymentPlan> filterByCriteria(PaymentPlanCriteria criteria) {
        return paymentPlanRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(PaymentPlan entity, PaymentPlanCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getName(), entity.getName()) &&
            FilterMatcher.matchesRange(criteria.getMonthsAfterHandover(), entity.getMonthsAfterHandover()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
