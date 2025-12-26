package com.yarmook.realstate.service.ai;

import com.yarmook.realstate.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PropertyEmbeddingScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyEmbeddingScheduler.class);

    private final ApplicationProperties applicationProperties;
    private final PropertyEmbeddingService propertyEmbeddingService;

    public PropertyEmbeddingScheduler(ApplicationProperties applicationProperties, PropertyEmbeddingService propertyEmbeddingService) {
        this.applicationProperties = applicationProperties;
        this.propertyEmbeddingService = propertyEmbeddingService;
    }

    //@Scheduled(cron = "${application.ai.indexer.cron:0 0 3 * * *}")
    public void scheduledIndexing() {
        if (!applicationProperties.getAi().getIndexer().isEnabled()) {
            LOG.trace("AI indexer disabled; skipping scheduled run");
            return;
        }
        propertyEmbeddingService.reindexAllProperties();
    }
}
