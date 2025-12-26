package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.service.ai.PropertyEmbeddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administrative endpoints for manual AI indexing workflows.
 */
@RestController
@RequestMapping("/api/admin/ai")
public class AiAdminResource {

    private static final Logger LOG = LoggerFactory.getLogger(AiAdminResource.class);

    private final PropertyEmbeddingService propertyEmbeddingService;

    public AiAdminResource(PropertyEmbeddingService propertyEmbeddingService) {
        this.propertyEmbeddingService = propertyEmbeddingService;
    }

    /**
     * {@code POST /api/admin/ai/reindex} : Trigger a manual property embeddings rebuild.
     *
     * @return {@link ResponseEntity} with status {@code 200 (OK)} once the reindex completes.
     */
    @PostMapping("/reindex")
    public ResponseEntity<Void> triggerReindex() {
        LOG.info("Manual AI property reindex requested by administrator");
        propertyEmbeddingService.reindexAllProperties();
        return ResponseEntity.ok().build();
    }
}
