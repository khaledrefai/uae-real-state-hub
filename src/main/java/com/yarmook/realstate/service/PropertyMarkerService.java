package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.PropertyMarker;
import com.yarmook.realstate.repository.PropertyMarkerRepository;
import com.yarmook.realstate.service.dto.PropertyMarkerDTO;
import com.yarmook.realstate.service.mapper.PropertyMarkerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.PropertyMarker}.
 */
@Service
public class PropertyMarkerService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyMarkerService.class);

    private final PropertyMarkerRepository propertyMarkerRepository;

    private final PropertyMarkerMapper propertyMarkerMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public PropertyMarkerService(
        PropertyMarkerRepository propertyMarkerRepository,
        PropertyMarkerMapper propertyMarkerMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.propertyMarkerRepository = propertyMarkerRepository;
        this.propertyMarkerMapper = propertyMarkerMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a propertyMarker.
     *
     * @param propertyMarkerDTO the entity to save.
     * @return the persisted entity.
     */
    public PropertyMarkerDTO save(PropertyMarkerDTO propertyMarkerDTO) {
        LOG.debug("Request to save PropertyMarker : {}", propertyMarkerDTO);
        PropertyMarker propertyMarker = propertyMarkerMapper.toEntity(propertyMarkerDTO);
        if (propertyMarker.getId() == null) {
            propertyMarker.setId(sequenceGeneratorService.generateSequence(PropertyMarker.SEQUENCE_NAME));
        }
        propertyMarker = propertyMarkerRepository.save(propertyMarker);
        return propertyMarkerMapper.toDto(propertyMarker);
    }

    /**
     * Update a propertyMarker.
     *
     * @param propertyMarkerDTO the entity to save.
     * @return the persisted entity.
     */
    public PropertyMarkerDTO update(PropertyMarkerDTO propertyMarkerDTO) {
        LOG.debug("Request to update PropertyMarker : {}", propertyMarkerDTO);
        PropertyMarker propertyMarker = propertyMarkerMapper.toEntity(propertyMarkerDTO);
        propertyMarker = propertyMarkerRepository.save(propertyMarker);
        return propertyMarkerMapper.toDto(propertyMarker);
    }

    /**
     * Partially update a propertyMarker.
     *
     * @param propertyMarkerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PropertyMarkerDTO> partialUpdate(PropertyMarkerDTO propertyMarkerDTO) {
        LOG.debug("Request to partially update PropertyMarker : {}", propertyMarkerDTO);

        return propertyMarkerRepository
            .findById(propertyMarkerDTO.getId())
            .map(existingPropertyMarker -> {
                propertyMarkerMapper.partialUpdate(existingPropertyMarker, propertyMarkerDTO);

                return existingPropertyMarker;
            })
            .map(propertyMarkerRepository::save)
            .map(propertyMarkerMapper::toDto);
    }

    /**
     *  Get all the propertyMarkers where Property is {@code null}.
     *  @return the list of entities.
     */
    public List<PropertyMarkerDTO> findAllWherePropertyIsNull() {
        LOG.debug("Request to get all propertyMarkers where Property is null");
        return StreamSupport.stream(propertyMarkerRepository.findAll().spliterator(), false)
            .filter(propertyMarker -> propertyMarker.getProperty() == null)
            .map(propertyMarkerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one propertyMarker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PropertyMarkerDTO> findOne(Long id) {
        LOG.debug("Request to get PropertyMarker : {}", id);
        return propertyMarkerRepository.findById(id).map(propertyMarkerMapper::toDto);
    }

    /**
     * Delete the propertyMarker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PropertyMarker : {}", id);
        propertyMarkerRepository.deleteById(id);
    }
}
