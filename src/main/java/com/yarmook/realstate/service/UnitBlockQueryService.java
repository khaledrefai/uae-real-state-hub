package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.UnitBlock;
import com.yarmook.realstate.repository.UnitBlockRepository;
import com.yarmook.realstate.service.criteria.UnitBlockCriteria;
import com.yarmook.realstate.service.dto.UnitBlockDTO;
import com.yarmook.realstate.service.mapper.UnitBlockMapper;
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
 * MongoDB-backed query service for {@link UnitBlock}.
 */
@Service
public class UnitBlockQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(UnitBlockQueryService.class);

    private final UnitBlockRepository unitBlockRepository;
    private final UnitBlockMapper unitBlockMapper;

    public UnitBlockQueryService(UnitBlockRepository unitBlockRepository, UnitBlockMapper unitBlockMapper) {
        this.unitBlockRepository = unitBlockRepository;
        this.unitBlockMapper = unitBlockMapper;
    }

    public Page<UnitBlockDTO> findByCriteria(UnitBlockCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<UnitBlock> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(unitBlockMapper::toDto);
    }

    public long countByCriteria(UnitBlockCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<UnitBlock> filterByCriteria(UnitBlockCriteria criteria) {
        return unitBlockRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(UnitBlock entity, UnitBlockCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getNormalizedType(), entity.getNormalizedType()) &&
            FilterMatcher.matches(criteria.getUnitType(), entity.getUnitType()) &&
            FilterMatcher.matches(criteria.getBedroomsAmount(), entity.getBedroomsAmount()) &&
            FilterMatcher.matches(criteria.getUnitBedrooms(), entity.getUnitBedrooms()) &&
            FilterMatcher.matches(criteria.getAreaUnit(), entity.getAreaUnit()) &&
            FilterMatcher.matchesRange(criteria.getUnitsAmount(), entity.getUnitsAmount()) &&
            FilterMatcher.matchesRange(criteria.getUnitsAreaFrom(), entity.getUnitsAreaFrom()) &&
            FilterMatcher.matchesRange(criteria.getUnitsAreaTo(), entity.getUnitsAreaTo()) &&
            FilterMatcher.matches(criteria.getUnitsAreaFromM2(), entity.getUnitsAreaFromM2()) &&
            FilterMatcher.matches(criteria.getUnitsAreaToM2(), entity.getUnitsAreaToM2()) &&
            FilterMatcher.matchesRange(criteria.getUnitsPriceFrom(), entity.getUnitsPriceFrom()) &&
            FilterMatcher.matchesRange(criteria.getUnitsPriceTo(), entity.getUnitsPriceTo()) &&
            FilterMatcher.matches(criteria.getPriceCurrency(), entity.getPriceCurrency()) &&
            FilterMatcher.matches(criteria.getTypicalImageUrl(), entity.getTypicalImageUrl()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
