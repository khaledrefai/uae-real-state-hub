package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.MapPoint;
import com.yarmook.realstate.repository.MapPointRepository;
import com.yarmook.realstate.service.criteria.MapPointCriteria;
import com.yarmook.realstate.service.dto.MapPointDTO;
import com.yarmook.realstate.service.mapper.MapPointMapper;
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
 * MongoDB-backed query service for {@link MapPoint}.
 */
@Service
public class MapPointQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(MapPointQueryService.class);

    private final MapPointRepository mapPointRepository;
    private final MapPointMapper mapPointMapper;

    public MapPointQueryService(MapPointRepository mapPointRepository, MapPointMapper mapPointMapper) {
        this.mapPointRepository = mapPointRepository;
        this.mapPointMapper = mapPointMapper;
    }

    public Page<MapPointDTO> findByCriteria(MapPointCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<MapPoint> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(mapPointMapper::toDto);
    }

    public long countByCriteria(MapPointCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<MapPoint> filterByCriteria(MapPointCriteria criteria) {
        return mapPointRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(MapPoint entity, MapPointCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getName(), entity.getName()) &&
            FilterMatcher.matchesRange(criteria.getDistanceKm(), entity.getDistanceKm()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
