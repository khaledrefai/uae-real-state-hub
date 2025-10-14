package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.UnitAvailability;
import com.yarmook.realstate.repository.UnitAvailabilityRepository;
import com.yarmook.realstate.service.dto.UnitAvailabilityDTO;
import com.yarmook.realstate.service.mapper.UnitAvailabilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.UnitAvailability}.
 */
@Service
public class UnitAvailabilityService {

    private static final Logger LOG = LoggerFactory.getLogger(UnitAvailabilityService.class);

    private final UnitAvailabilityRepository unitAvailabilityRepository;

    private final UnitAvailabilityMapper unitAvailabilityMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public UnitAvailabilityService(
        UnitAvailabilityRepository unitAvailabilityRepository,
        UnitAvailabilityMapper unitAvailabilityMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.unitAvailabilityRepository = unitAvailabilityRepository;
        this.unitAvailabilityMapper = unitAvailabilityMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a unitAvailability.
     *
     * @param unitAvailabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public UnitAvailabilityDTO save(UnitAvailabilityDTO unitAvailabilityDTO) {
        LOG.debug("Request to save UnitAvailability : {}", unitAvailabilityDTO);
        UnitAvailability unitAvailability = unitAvailabilityMapper.toEntity(unitAvailabilityDTO);
        if (unitAvailability.getId() == null) {
            unitAvailability.setId(sequenceGeneratorService.generateSequence(UnitAvailability.SEQUENCE_NAME));
        }
        unitAvailability = unitAvailabilityRepository.save(unitAvailability);
        return unitAvailabilityMapper.toDto(unitAvailability);
    }

    /**
     * Update a unitAvailability.
     *
     * @param unitAvailabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public UnitAvailabilityDTO update(UnitAvailabilityDTO unitAvailabilityDTO) {
        LOG.debug("Request to update UnitAvailability : {}", unitAvailabilityDTO);
        UnitAvailability unitAvailability = unitAvailabilityMapper.toEntity(unitAvailabilityDTO);
        unitAvailability = unitAvailabilityRepository.save(unitAvailability);
        return unitAvailabilityMapper.toDto(unitAvailability);
    }

    /**
     * Partially update a unitAvailability.
     *
     * @param unitAvailabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitAvailabilityDTO> partialUpdate(UnitAvailabilityDTO unitAvailabilityDTO) {
        LOG.debug("Request to partially update UnitAvailability : {}", unitAvailabilityDTO);

        return unitAvailabilityRepository
            .findById(unitAvailabilityDTO.getId())
            .map(existingUnitAvailability -> {
                unitAvailabilityMapper.partialUpdate(existingUnitAvailability, unitAvailabilityDTO);

                return existingUnitAvailability;
            })
            .map(unitAvailabilityRepository::save)
            .map(unitAvailabilityMapper::toDto);
    }

    /**
     * Get one unitAvailability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<UnitAvailabilityDTO> findOne(Long id) {
        LOG.debug("Request to get UnitAvailability : {}", id);
        return unitAvailabilityRepository.findById(id).map(unitAvailabilityMapper::toDto);
    }

    /**
     * Delete the unitAvailability by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UnitAvailability : {}", id);
        unitAvailabilityRepository.deleteById(id);
    }
}
