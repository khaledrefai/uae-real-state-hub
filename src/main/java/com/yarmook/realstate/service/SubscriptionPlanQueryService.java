package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.SubscriptionPlan;
import com.yarmook.realstate.repository.SubscriptionPlanRepository;
import com.yarmook.realstate.service.criteria.SubscriptionPlanCriteria;
import com.yarmook.realstate.service.dto.SubscriptionPlanDTO;
import com.yarmook.realstate.service.mapper.SubscriptionPlanMapper;
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
 * MongoDB-backed query service for {@link SubscriptionPlan}.
 */
@Service
public class SubscriptionPlanQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionPlanQueryService.class);

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    public SubscriptionPlanQueryService(
        SubscriptionPlanRepository subscriptionPlanRepository,
        SubscriptionPlanMapper subscriptionPlanMapper
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
    }

    public Page<SubscriptionPlanDTO> findByCriteria(SubscriptionPlanCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<SubscriptionPlan> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(subscriptionPlanMapper::toDto);
    }

    public long countByCriteria(SubscriptionPlanCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<SubscriptionPlan> filterByCriteria(SubscriptionPlanCriteria criteria) {
        return subscriptionPlanRepository
            .findAll()
            .stream()
            .filter(entity -> matchesCriteria(entity, criteria))
            .collect(Collectors.toList());
    }

    private boolean matchesCriteria(SubscriptionPlan entity, SubscriptionPlanCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getCode(), entity.getCode()) &&
            FilterMatcher.matches(criteria.getName(), entity.getName()) &&
            FilterMatcher.matchesRange(criteria.getPriceMonthly(), entity.getPriceMonthly()) &&
            FilterMatcher.matchesRange(criteria.getPriceYearly(), entity.getPriceYearly()) &&
            FilterMatcher.matches(criteria.getCurrency(), entity.getCurrency()) &&
            FilterMatcher.matches(criteria.getStripePriceMonthlyId(), entity.getStripePriceMonthlyId()) &&
            FilterMatcher.matches(criteria.getStripePriceYearlyId(), entity.getStripePriceYearlyId()) &&
            FilterMatcher.matchesRange(criteria.getMaxListings(), entity.getMaxListings())
        );
    }
}
