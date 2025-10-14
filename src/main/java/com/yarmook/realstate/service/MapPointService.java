package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.MapPoint;
import com.yarmook.realstate.repository.MapPointRepository;
import com.yarmook.realstate.service.dto.MapPointDTO;
import com.yarmook.realstate.service.mapper.MapPointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.MapPoint}.
 */
@Service
public class MapPointService {

    private static final Logger LOG = LoggerFactory.getLogger(MapPointService.class);

    private final MapPointRepository mapPointRepository;

    private final MapPointMapper mapPointMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public MapPointService(
        MapPointRepository mapPointRepository,
        MapPointMapper mapPointMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.mapPointRepository = mapPointRepository;
        this.mapPointMapper = mapPointMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a mapPoint.
     *
     * @param mapPointDTO the entity to save.
     * @return the persisted entity.
     */
    public MapPointDTO save(MapPointDTO mapPointDTO) {
        LOG.debug("Request to save MapPoint : {}", mapPointDTO);
        MapPoint mapPoint = mapPointMapper.toEntity(mapPointDTO);
        if (mapPoint.getId() == null) {
            mapPoint.setId(sequenceGeneratorService.generateSequence(MapPoint.SEQUENCE_NAME));
        }
        mapPoint = mapPointRepository.save(mapPoint);
        return mapPointMapper.toDto(mapPoint);
    }

    /**
     * Update a mapPoint.
     *
     * @param mapPointDTO the entity to save.
     * @return the persisted entity.
     */
    public MapPointDTO update(MapPointDTO mapPointDTO) {
        LOG.debug("Request to update MapPoint : {}", mapPointDTO);
        MapPoint mapPoint = mapPointMapper.toEntity(mapPointDTO);
        mapPoint = mapPointRepository.save(mapPoint);
        return mapPointMapper.toDto(mapPoint);
    }

    /**
     * Partially update a mapPoint.
     *
     * @param mapPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MapPointDTO> partialUpdate(MapPointDTO mapPointDTO) {
        LOG.debug("Request to partially update MapPoint : {}", mapPointDTO);

        return mapPointRepository
            .findById(mapPointDTO.getId())
            .map(existingMapPoint -> {
                mapPointMapper.partialUpdate(existingMapPoint, mapPointDTO);

                return existingMapPoint;
            })
            .map(mapPointRepository::save)
            .map(mapPointMapper::toDto);
    }

    /**
     * Get one mapPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MapPointDTO> findOne(Long id) {
        LOG.debug("Request to get MapPoint : {}", id);
        return mapPointRepository.findById(id).map(mapPointMapper::toDto);
    }

    /**
     * Delete the mapPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MapPoint : {}", id);
        mapPointRepository.deleteById(id);
    }
}
