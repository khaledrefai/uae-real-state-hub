package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.UnitBlock;
import com.yarmook.realstate.repository.UnitBlockRepository;
import com.yarmook.realstate.service.dto.UnitBlockDTO;
import com.yarmook.realstate.service.mapper.UnitBlockMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.UnitBlock}.
 */
@Service
public class UnitBlockService {

    private static final Logger LOG = LoggerFactory.getLogger(UnitBlockService.class);

    private final UnitBlockRepository unitBlockRepository;

    private final UnitBlockMapper unitBlockMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public UnitBlockService(
        UnitBlockRepository unitBlockRepository,
        UnitBlockMapper unitBlockMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.unitBlockRepository = unitBlockRepository;
        this.unitBlockMapper = unitBlockMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a unitBlock.
     *
     * @param unitBlockDTO the entity to save.
     * @return the persisted entity.
     */
    public UnitBlockDTO save(UnitBlockDTO unitBlockDTO) {
        LOG.debug("Request to save UnitBlock : {}", unitBlockDTO);
        UnitBlock unitBlock = unitBlockMapper.toEntity(unitBlockDTO);
        if (unitBlock.getId() == null) {
            unitBlock.setId(sequenceGeneratorService.generateSequence(UnitBlock.SEQUENCE_NAME));
        }
        unitBlock = unitBlockRepository.save(unitBlock);
        return unitBlockMapper.toDto(unitBlock);
    }

    /**
     * Update a unitBlock.
     *
     * @param unitBlockDTO the entity to save.
     * @return the persisted entity.
     */
    public UnitBlockDTO update(UnitBlockDTO unitBlockDTO) {
        LOG.debug("Request to update UnitBlock : {}", unitBlockDTO);
        UnitBlock unitBlock = unitBlockMapper.toEntity(unitBlockDTO);
        unitBlock = unitBlockRepository.save(unitBlock);
        return unitBlockMapper.toDto(unitBlock);
    }

    /**
     * Partially update a unitBlock.
     *
     * @param unitBlockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitBlockDTO> partialUpdate(UnitBlockDTO unitBlockDTO) {
        LOG.debug("Request to partially update UnitBlock : {}", unitBlockDTO);

        return unitBlockRepository
            .findById(unitBlockDTO.getId())
            .map(existingUnitBlock -> {
                unitBlockMapper.partialUpdate(existingUnitBlock, unitBlockDTO);

                return existingUnitBlock;
            })
            .map(unitBlockRepository::save)
            .map(unitBlockMapper::toDto);
    }

    /**
     * Get one unitBlock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UnitBlockDTO> findOne(Long id) {
        LOG.debug("Request to get UnitBlock : {}", id);
        return unitBlockRepository.findById(id).map(unitBlockMapper::toDto);
    }

    /**
     * Delete the unitBlock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UnitBlock : {}", id);
        unitBlockRepository.deleteById(id);
    }
}
