package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.Facility;
import com.yarmook.realstate.repository.FacilityRepository;
import com.yarmook.realstate.service.criteria.FacilityCriteria;
import com.yarmook.realstate.service.dto.FacilityDTO;
import com.yarmook.realstate.service.mapper.FacilityMapper;
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
 * MongoDB-backed query service for {@link Facility}.
 */
@Service
public class FacilityQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityQueryService.class);

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public FacilityQueryService(FacilityRepository facilityRepository, FacilityMapper facilityMapper) {
        this.facilityRepository = facilityRepository;
        this.facilityMapper = facilityMapper;
    }

    public Page<FacilityDTO> findByCriteria(FacilityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<Facility> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(facilityMapper::toDto);
    }

    public long countByCriteria(FacilityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<Facility> filterByCriteria(FacilityCriteria criteria) {
        return facilityRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(Facility entity, FacilityCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getName(), entity.getName()) &&
            FilterMatcher.matches(criteria.getSource(), entity.getSource()) &&
            FilterMatcher.matches(criteria.getImageUrl(), entity.getImageUrl()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
