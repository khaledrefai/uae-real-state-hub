package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.repository.PropertyRepository;
import com.yarmook.realstate.service.dto.PropertyDTO;
import com.yarmook.realstate.service.mapper.PropertyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.Property}.
 */
@Service
public class PropertyService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyService.class);

    private final PropertyRepository propertyRepository;

    private final PropertyMapper propertyMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public PropertyService(
        PropertyRepository propertyRepository,
        PropertyMapper propertyMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a property.
     *
     * @param propertyDTO the entity to save.
     * @return the persisted entity.
     */
    public PropertyDTO save(PropertyDTO propertyDTO) {
        LOG.debug("Request to save Property : {}", propertyDTO);
        Property property = propertyMapper.toEntity(propertyDTO);
        if (property.getId() == null) {
            property.setId(sequenceGeneratorService.generateSequence(Property.SEQUENCE_NAME));
        }
        property = propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    /**
     * Update a property.
     *
     * @param propertyDTO the entity to save.
     * @return the persisted entity.
     */
    public PropertyDTO update(PropertyDTO propertyDTO) {
        LOG.debug("Request to update Property : {}", propertyDTO);
        Property property = propertyMapper.toEntity(propertyDTO);
        property = propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    /**
     * Partially update a property.
     *
     * @param propertyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PropertyDTO> partialUpdate(PropertyDTO propertyDTO) {
        LOG.debug("Request to partially update Property : {}", propertyDTO);

        return propertyRepository
            .findById(propertyDTO.getId())
            .map(existingProperty -> {
                propertyMapper.partialUpdate(existingProperty, propertyDTO);

                return existingProperty;
            })
            .map(propertyRepository::save)
            .map(propertyMapper::toDto);
    }

    /**
     * Get one property by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PropertyDTO> findOne(Long id) {
        LOG.debug("Request to get Property : {}", id);
        return propertyRepository.findById(id).map(propertyMapper::toDto);
    }

    /**
     * Delete the property by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Property : {}", id);
        propertyRepository.deleteById(id);
    }
}
