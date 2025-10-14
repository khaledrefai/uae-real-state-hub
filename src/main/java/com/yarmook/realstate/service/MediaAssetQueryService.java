package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.MediaAsset;
import com.yarmook.realstate.repository.MediaAssetRepository;
import com.yarmook.realstate.service.criteria.MediaAssetCriteria;
import com.yarmook.realstate.service.dto.MediaAssetDTO;
import com.yarmook.realstate.service.mapper.MediaAssetMapper;
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
 * MongoDB-backed query service for {@link MediaAsset}.
 */
@Service
public class MediaAssetQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(MediaAssetQueryService.class);

    private final MediaAssetRepository mediaAssetRepository;
    private final MediaAssetMapper mediaAssetMapper;

    public MediaAssetQueryService(MediaAssetRepository mediaAssetRepository, MediaAssetMapper mediaAssetMapper) {
        this.mediaAssetRepository = mediaAssetRepository;
        this.mediaAssetMapper = mediaAssetMapper;
    }

    public Page<MediaAssetDTO> findByCriteria(MediaAssetCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<MediaAsset> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(mediaAssetMapper::toDto);
    }

    public long countByCriteria(MediaAssetCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<MediaAsset> filterByCriteria(MediaAssetCriteria criteria) {
        return mediaAssetRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(MediaAsset entity, MediaAssetCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getKind(), entity.getKind()) &&
            FilterMatcher.matches(criteria.getUrl(), entity.getUrl()) &&
            FilterMatcher.matches(criteria.getMimeType(), entity.getMimeType()) &&
            FilterMatcher.matchesRange(criteria.getWidth(), entity.getWidth()) &&
            FilterMatcher.matchesRange(criteria.getHeight(), entity.getHeight()) &&
            FilterMatcher.matchesRange(criteria.getMediaSize(), entity.getMediaSize()) &&
            FilterMatcher.matches(criteria.getSource(), entity.getSource()) &&
            FilterMatcher.matches(criteria.getTitle(), entity.getTitle()) &&
            FilterMatcher.matches(criteria.getAltText(), entity.getAltText()) &&
            FilterMatcher.matchesRange(criteria.getSortOrder(), entity.getSortOrder()) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
