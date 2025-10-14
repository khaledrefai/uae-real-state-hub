package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.UnitAvailability;
import com.yarmook.realstate.repository.UnitAvailabilityRepository;
import com.yarmook.realstate.service.criteria.UnitAvailabilityCriteria;
import com.yarmook.realstate.service.dto.UnitAvailabilityDTO;
import com.yarmook.realstate.service.mapper.UnitAvailabilityMapper;
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
 * MongoDB-backed query service for {@link UnitAvailability}.
 */
@Service
public class UnitAvailabilityQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(UnitAvailabilityQueryService.class);

    private final UnitAvailabilityRepository unitAvailabilityRepository;
    private final UnitAvailabilityMapper unitAvailabilityMapper;

    public UnitAvailabilityQueryService(
        UnitAvailabilityRepository unitAvailabilityRepository,
        UnitAvailabilityMapper unitAvailabilityMapper
    ) {
        this.unitAvailabilityRepository = unitAvailabilityRepository;
        this.unitAvailabilityMapper = unitAvailabilityMapper;
    }

    public Page<UnitAvailabilityDTO> findByCriteria(UnitAvailabilityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<UnitAvailability> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(unitAvailabilityMapper::toDto);
    }

    public long countByCriteria(UnitAvailabilityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<UnitAvailability> filterByCriteria(UnitAvailabilityCriteria criteria) {
        return unitAvailabilityRepository
            .findAll()
            .stream()
            .filter(entity -> matchesCriteria(entity, criteria))
            .collect(Collectors.toList());
    }

    private boolean matchesCriteria(UnitAvailability entity, UnitAvailabilityCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getBuildingName(), entity.getBuildingName()) &&
            FilterMatcher.matchesRange(criteria.getAreaFrom(), entity.getAreaFrom()) &&
            FilterMatcher.matches(criteria.getAreaUnit(), entity.getAreaUnit()) &&
            FilterMatcher.matches(criteria.getBedroomType(), entity.getBedroomType()) &&
            FilterMatcher.matchesRange(criteria.getLastUpdated(), entity.getLastUpdated()) &&
            FilterMatcher.matches(criteria.getPriceCurrency(), entity.getPriceCurrency()) &&
            FilterMatcher.matchesRange(criteria.getPriceFrom(), entity.getPriceFrom()) &&
            FilterMatcher.matchesRange(criteria.getPriceTo(), entity.getPriceTo()) &&
            FilterMatcher.matchesRange(criteria.getUnitsAvailable(), entity.getUnitsAvailable()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
