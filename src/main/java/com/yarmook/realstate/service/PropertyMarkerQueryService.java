package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.PropertyMarker;
import com.yarmook.realstate.repository.PropertyMarkerRepository;
import com.yarmook.realstate.service.criteria.PropertyMarkerCriteria;
import com.yarmook.realstate.service.dto.PropertyMarkerDTO;
import com.yarmook.realstate.service.mapper.PropertyMarkerMapper;
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
 * MongoDB-backed query service for {@link PropertyMarker}.
 */
@Service
public class PropertyMarkerQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyMarkerQueryService.class);

    private final PropertyMarkerRepository propertyMarkerRepository;
    private final PropertyMarkerMapper propertyMarkerMapper;

    public PropertyMarkerQueryService(PropertyMarkerRepository propertyMarkerRepository, PropertyMarkerMapper propertyMarkerMapper) {
        this.propertyMarkerRepository = propertyMarkerRepository;
        this.propertyMarkerMapper = propertyMarkerMapper;
    }

    public Page<PropertyMarkerDTO> findByCriteria(PropertyMarkerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<PropertyMarker> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(propertyMarkerMapper::toDto);
    }

    public long countByCriteria(PropertyMarkerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<PropertyMarker> filterByCriteria(PropertyMarkerCriteria criteria) {
        return propertyMarkerRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(PropertyMarker entity, PropertyMarkerCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matchesRange(criteria.getExtId(), entity.getExtId()) &&
            FilterMatcher.matches(criteria.getArea(), entity.getArea()) &&
            FilterMatcher.matchesRange(criteria.getCompletionDate(), entity.getCompletionDate()) &&
            FilterMatcher.matchesRange(criteria.getLatitude(), entity.getLatitude()) &&
            FilterMatcher.matchesRange(criteria.getLongitude(), entity.getLongitude()) &&
            FilterMatcher.matches(criteria.getName(), entity.getName()) &&
            FilterMatcher.matches(criteria.getDeveloper(), entity.getDeveloper()) &&
            FilterMatcher.matches(criteria.getStatus(), entity.getStatus()) &&
            FilterMatcher.matches(criteria.getSaleStatus(), entity.getSaleStatus()) &&
            FilterMatcher.matchesRange(criteria.getMinPrice(), entity.getMinPrice()) &&
            FilterMatcher.matches(criteria.getCoverUrl(), entity.getCoverUrl()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
