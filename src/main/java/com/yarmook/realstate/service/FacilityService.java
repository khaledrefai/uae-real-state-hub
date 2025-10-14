package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.Facility;
import com.yarmook.realstate.repository.FacilityRepository;
import com.yarmook.realstate.service.dto.FacilityDTO;
import com.yarmook.realstate.service.mapper.FacilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.Facility}.
 */
@Service
public class FacilityService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityService.class);

    private final FacilityRepository facilityRepository;

    private final FacilityMapper facilityMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public FacilityService(
        FacilityRepository facilityRepository,
        FacilityMapper facilityMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.facilityRepository = facilityRepository;
        this.facilityMapper = facilityMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a facility.
     *
     * @param facilityDTO the entity to save.
     * @return the persisted entity.
     */
    public FacilityDTO save(FacilityDTO facilityDTO) {
        LOG.debug("Request to save Facility : {}", facilityDTO);
        Facility facility = facilityMapper.toEntity(facilityDTO);
        if (facility.getId() == null) {
            facility.setId(sequenceGeneratorService.generateSequence(Facility.SEQUENCE_NAME));
        }
        facility = facilityRepository.save(facility);
        return facilityMapper.toDto(facility);
    }

    /**
     * Update a facility.
     *
     * @param facilityDTO the entity to save.
     * @return the persisted entity.
     */
    public FacilityDTO update(FacilityDTO facilityDTO) {
        LOG.debug("Request to update Facility : {}", facilityDTO);
        Facility facility = facilityMapper.toEntity(facilityDTO);
        facility = facilityRepository.save(facility);
        return facilityMapper.toDto(facility);
    }

    /**
     * Partially update a facility.
     *
     * @param facilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityDTO> partialUpdate(FacilityDTO facilityDTO) {
        LOG.debug("Request to partially update Facility : {}", facilityDTO);

        return facilityRepository
            .findById(facilityDTO.getId())
            .map(existingFacility -> {
                facilityMapper.partialUpdate(existingFacility, facilityDTO);

                return existingFacility;
            })
            .map(facilityRepository::save)
            .map(facilityMapper::toDto);
    }

    /**
     * Get one facility by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<FacilityDTO> findOne(Long id) {
        LOG.debug("Request to get Facility : {}", id);
        return facilityRepository.findById(id).map(facilityMapper::toDto);
    }

    /**
     * Delete the facility by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Facility : {}", id);
        facilityRepository.deleteById(id);
    }
}
