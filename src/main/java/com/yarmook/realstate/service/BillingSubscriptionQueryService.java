package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.BillingSubscription;
import com.yarmook.realstate.repository.BillingSubscriptionRepository;
import com.yarmook.realstate.service.criteria.BillingSubscriptionCriteria;
import com.yarmook.realstate.service.dto.BillingSubscriptionDTO;
import com.yarmook.realstate.service.mapper.BillingSubscriptionMapper;
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
 * MongoDB-backed query service for {@link BillingSubscription}.
 */
@Service
public class BillingSubscriptionQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingSubscriptionQueryService.class);

    private final BillingSubscriptionRepository billingSubscriptionRepository;
    private final BillingSubscriptionMapper billingSubscriptionMapper;

    public BillingSubscriptionQueryService(
        BillingSubscriptionRepository billingSubscriptionRepository,
        BillingSubscriptionMapper billingSubscriptionMapper
    ) {
        this.billingSubscriptionRepository = billingSubscriptionRepository;
        this.billingSubscriptionMapper = billingSubscriptionMapper;
    }

    public Page<BillingSubscriptionDTO> findByCriteria(BillingSubscriptionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<BillingSubscription> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(billingSubscriptionMapper::toDto);
    }

    public long countByCriteria(BillingSubscriptionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<BillingSubscription> filterByCriteria(BillingSubscriptionCriteria criteria) {
        return billingSubscriptionRepository
            .findAll()
            .stream()
            .filter(entity -> matchesCriteria(entity, criteria))
            .collect(Collectors.toList());
    }

    private boolean matchesCriteria(BillingSubscription entity, BillingSubscriptionCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getStatus(), entity.getStatus()) &&
            FilterMatcher.matchesRange(criteria.getStartDate(), entity.getStartDate()) &&
            FilterMatcher.matchesRange(criteria.getEndDate(), entity.getEndDate()) &&
            FilterMatcher.matches(criteria.getStripeCustomerId(), entity.getStripeCustomerId()) &&
            FilterMatcher.matches(criteria.getStripeSubscriptionId(), entity.getStripeSubscriptionId()) &&
            FilterMatcher.matches(criteria.getCancelAtPeriodEnd(), entity.getCancelAtPeriodEnd()) &&
            FilterMatcher.matches(criteria.getIsActive(), entity.getIsActive()) &&
            FilterMatcher.matchesRange(criteria.getSiteId(), entity.getSite() != null ? entity.getSite().getId() : null)
        );
    }
}
