package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.AgentSite;
import com.yarmook.realstate.repository.AgentSiteRepository;
import com.yarmook.realstate.service.dto.AgentSiteDTO;
import com.yarmook.realstate.service.mapper.AgentSiteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.AgentSite}.
 */
@Service
public class AgentSiteService {

    private static final Logger LOG = LoggerFactory.getLogger(AgentSiteService.class);

    private final AgentSiteRepository agentSiteRepository;

    private final AgentSiteMapper agentSiteMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public AgentSiteService(
        AgentSiteRepository agentSiteRepository,
        AgentSiteMapper agentSiteMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.agentSiteRepository = agentSiteRepository;
        this.agentSiteMapper = agentSiteMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a agentSite.
     *
     * @param agentSiteDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentSiteDTO save(AgentSiteDTO agentSiteDTO) {
        LOG.debug("Request to save AgentSite : {}", agentSiteDTO);
        AgentSite agentSite = agentSiteMapper.toEntity(agentSiteDTO);
        if (agentSite.getId() == null) {
            agentSite.setId(sequenceGeneratorService.generateSequence(AgentSite.SEQUENCE_NAME));
        }
        agentSite = agentSiteRepository.save(agentSite);
        return agentSiteMapper.toDto(agentSite);
    }

    /**
     * Update a agentSite.
     *
     * @param agentSiteDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentSiteDTO update(AgentSiteDTO agentSiteDTO) {
        LOG.debug("Request to update AgentSite : {}", agentSiteDTO);
        AgentSite agentSite = agentSiteMapper.toEntity(agentSiteDTO);
        agentSite = agentSiteRepository.save(agentSite);
        return agentSiteMapper.toDto(agentSite);
    }

    /**
     * Partially update a agentSite.
     *
     * @param agentSiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgentSiteDTO> partialUpdate(AgentSiteDTO agentSiteDTO) {
        LOG.debug("Request to partially update AgentSite : {}", agentSiteDTO);

        return agentSiteRepository
            .findById(agentSiteDTO.getId())
            .map(existingAgentSite -> {
                agentSiteMapper.partialUpdate(existingAgentSite, agentSiteDTO);

                return existingAgentSite;
            })
            .map(agentSiteRepository::save)
            .map(agentSiteMapper::toDto);
    }

    /**
     * Get one agentSite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AgentSiteDTO> findOne(Long id) {
        LOG.debug("Request to get AgentSite : {}", id);
        return agentSiteRepository.findById(id).map(agentSiteMapper::toDto);
    }

    /**
     * Delete the agentSite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AgentSite : {}", id);
        agentSiteRepository.deleteById(id);
    }
}
