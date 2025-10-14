package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.AgentProfile;
import com.yarmook.realstate.repository.AgentProfileRepository;
import com.yarmook.realstate.service.dto.AgentProfileDTO;
import com.yarmook.realstate.service.mapper.AgentProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.AgentProfile}.
 */
@Service
public class AgentProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentProfileService.class);

    private final AgentProfileRepository agentProfileRepository;

    private final AgentProfileMapper agentProfileMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public AgentProfileService(
        AgentProfileRepository agentProfileRepository,
        AgentProfileMapper agentProfileMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.agentProfileRepository = agentProfileRepository;
        this.agentProfileMapper = agentProfileMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a agentProfile.
     *
     * @param agentProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentProfileDTO save(AgentProfileDTO agentProfileDTO) {
        LOG.debug("Request to save AgentProfile : {}", agentProfileDTO);
        AgentProfile agentProfile = agentProfileMapper.toEntity(agentProfileDTO);
        if (agentProfile.getId() == null) {
            agentProfile.setId(sequenceGeneratorService.generateSequence(AgentProfile.SEQUENCE_NAME));
        }
        agentProfile = agentProfileRepository.save(agentProfile);
        return agentProfileMapper.toDto(agentProfile);
    }

    /**
     * Update a agentProfile.
     *
     * @param agentProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentProfileDTO update(AgentProfileDTO agentProfileDTO) {
        LOG.debug("Request to update AgentProfile : {}", agentProfileDTO);
        AgentProfile agentProfile = agentProfileMapper.toEntity(agentProfileDTO);
        agentProfile = agentProfileRepository.save(agentProfile);
        return agentProfileMapper.toDto(agentProfile);
    }

    /**
     * Partially update a agentProfile.
     *
     * @param agentProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgentProfileDTO> partialUpdate(AgentProfileDTO agentProfileDTO) {
        LOG.debug("Request to partially update AgentProfile : {}", agentProfileDTO);

        return agentProfileRepository
            .findById(agentProfileDTO.getId())
            .map(existingAgentProfile -> {
                agentProfileMapper.partialUpdate(existingAgentProfile, agentProfileDTO);

                return existingAgentProfile;
            })
            .map(agentProfileRepository::save)
            .map(agentProfileMapper::toDto);
    }

    /**
     * Get one agentProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AgentProfileDTO> findOne(Long id) {
        LOG.debug("Request to get AgentProfile : {}", id);
        return agentProfileRepository.findById(id).map(agentProfileMapper::toDto);
    }

    /**
     * Delete the agentProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AgentProfile : {}", id);
        agentProfileRepository.deleteById(id);
    }
}
