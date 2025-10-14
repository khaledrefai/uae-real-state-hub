package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.MediaAsset;
import com.yarmook.realstate.repository.MediaAssetRepository;
import com.yarmook.realstate.service.dto.MediaAssetDTO;
import com.yarmook.realstate.service.mapper.MediaAssetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.yarmook.realstate.domain.MediaAsset}.
 */
@Service
public class MediaAssetService {

    private static final Logger LOG = LoggerFactory.getLogger(MediaAssetService.class);

    private final MediaAssetRepository mediaAssetRepository;

    private final MediaAssetMapper mediaAssetMapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public MediaAssetService(
        MediaAssetRepository mediaAssetRepository,
        MediaAssetMapper mediaAssetMapper,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.mediaAssetRepository = mediaAssetRepository;
        this.mediaAssetMapper = mediaAssetMapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a mediaAsset.
     *
     * @param mediaAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaAssetDTO save(MediaAssetDTO mediaAssetDTO) {
        LOG.debug("Request to save MediaAsset : {}", mediaAssetDTO);
        MediaAsset mediaAsset = mediaAssetMapper.toEntity(mediaAssetDTO);
        if (mediaAsset.getId() == null) {
            mediaAsset.setId(sequenceGeneratorService.generateSequence(MediaAsset.SEQUENCE_NAME));
        }
        mediaAsset = mediaAssetRepository.save(mediaAsset);
        return mediaAssetMapper.toDto(mediaAsset);
    }

    /**
     * Update a mediaAsset.
     *
     * @param mediaAssetDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaAssetDTO update(MediaAssetDTO mediaAssetDTO) {
        LOG.debug("Request to update MediaAsset : {}", mediaAssetDTO);
        MediaAsset mediaAsset = mediaAssetMapper.toEntity(mediaAssetDTO);
        mediaAsset = mediaAssetRepository.save(mediaAsset);
        return mediaAssetMapper.toDto(mediaAsset);
    }

    /**
     * Partially update a mediaAsset.
     *
     * @param mediaAssetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MediaAssetDTO> partialUpdate(MediaAssetDTO mediaAssetDTO) {
        LOG.debug("Request to partially update MediaAsset : {}", mediaAssetDTO);

        return mediaAssetRepository
            .findById(mediaAssetDTO.getId())
            .map(existingMediaAsset -> {
                mediaAssetMapper.partialUpdate(existingMediaAsset, mediaAssetDTO);

                return existingMediaAsset;
            })
            .map(mediaAssetRepository::save)
            .map(mediaAssetMapper::toDto);
    }

    /**
     * Get one mediaAsset by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MediaAssetDTO> findOne(Long id) {
        LOG.debug("Request to get MediaAsset : {}", id);
        return mediaAssetRepository.findById(id).map(mediaAssetMapper::toDto);
    }

    /**
     * Delete the mediaAsset by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MediaAsset : {}", id);
        mediaAssetRepository.deleteById(id);
    }
}
