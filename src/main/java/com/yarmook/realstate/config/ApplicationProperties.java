package com.yarmook.realstate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Uae Real State Hub.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final External external = new External();
    private final Ai ai = new Ai();

    // jhipster-needle-application-properties-property

    public External getExternal() {
        return external;
    }

    public Ai getAi() {
        return ai;
    }

    // jhipster-needle-application-properties-property-getter
    public static class External {

        private final Reely reely = new Reely();

        public Reely getReely() {
            return reely;
        }

        public static class Reely {

            private boolean enabled = true;

            private String baseUrl = "https://search-listings-production.up.railway.app/v1";

            private String apiKey;

            private String apiKeyHeader = "x-api-key";

            private String propertiesPath = "/properties";

            private String propertyMarkersPath = "/property-markers";
            private String propertyDetailPath = "/properties/%s";

            private Integer pageSize;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }

            public String getApiKeyHeader() {
                return apiKeyHeader;
            }

            public void setApiKeyHeader(String apiKeyHeader) {
                this.apiKeyHeader = apiKeyHeader;
            }

            public String getPropertiesPath() {
                return propertiesPath;
            }

            public void setPropertiesPath(String propertiesPath) {
                this.propertiesPath = propertiesPath;
            }

            public String getPropertyDetailPath() {
                return propertyDetailPath;
            }

            public void setPropertyDetailPath(String propertyDetailPath) {
                this.propertyDetailPath = propertyDetailPath;
            }

            public String getPropertyMarkersPath() {
                return propertyMarkersPath;
            }

            public void setPropertyMarkersPath(String propertyMarkersPath) {
                this.propertyMarkersPath = propertyMarkersPath;
            }

            public Integer getPageSize() {
                return pageSize;
            }

            public void setPageSize(Integer pageSize) {
                this.pageSize = pageSize;
            }
        }
    }

    // jhipster-needle-application-properties-property-class

    public static class Ai {

        private boolean enabled = true;
        private final OpenAi openAi = new OpenAi();
        private final Qdrant qdrant = new Qdrant();
        private final Indexer indexer = new Indexer();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public OpenAi getOpenAi() {
            return openAi;
        }

        public Qdrant getQdrant() {
            return qdrant;
        }

        public Indexer getIndexer() {
            return indexer;
        }

        public static class OpenAi {

            private String apiKey;
            private String baseUrl = "https://api.openai.com/v1";
            private String embeddingsModel = "text-embedding-3-small";
            private String chatModel = "gpt-4o-mini";
            private Double temperature = 0.2d;
            private Integer maxRetries = 3;
            private Long retryBackoffMillis = 2000L;

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }

            public String getBaseUrl() {
                return baseUrl;
            }

            public void setBaseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
            }

            public String getEmbeddingsModel() {
                return embeddingsModel;
            }

            public void setEmbeddingsModel(String embeddingsModel) {
                this.embeddingsModel = embeddingsModel;
            }

            public String getChatModel() {
                return chatModel;
            }

            public void setChatModel(String chatModel) {
                this.chatModel = chatModel;
            }

            public Double getTemperature() {
                return temperature;
            }

            public void setTemperature(Double temperature) {
                this.temperature = temperature;
            }

            public Integer getMaxRetries() {
                return maxRetries;
            }

            public void setMaxRetries(Integer maxRetries) {
                this.maxRetries = maxRetries;
            }

            public Long getRetryBackoffMillis() {
                return retryBackoffMillis;
            }

            public void setRetryBackoffMillis(Long retryBackoffMillis) {
                this.retryBackoffMillis = retryBackoffMillis;
            }
        }

        public static class Qdrant {

            private String url = "http://localhost:6333";
            private String apiKey;
            private String collection = "reelly";
            private Integer batchSize = 32;
            private Integer searchLimit = 12;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getApiKey() {
                return apiKey;
            }

            public void setApiKey(String apiKey) {
                this.apiKey = apiKey;
            }

            public String getCollection() {
                return collection;
            }

            public void setCollection(String collection) {
                this.collection = collection;
            }

            public Integer getBatchSize() {
                return batchSize;
            }

            public void setBatchSize(Integer batchSize) {
                this.batchSize = batchSize;
            }

            public Integer getSearchLimit() {
                return searchLimit;
            }

            public void setSearchLimit(Integer searchLimit) {
                this.searchLimit = searchLimit;
            }
        }

        public static class Indexer {

            private boolean enabled = true;
            private String cron = "0 0 3 * * *";
            private Integer pageSize = 50;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getCron() {
                return cron;
            }

            public void setCron(String cron) {
                this.cron = cron;
            }

            public Integer getPageSize() {
                return pageSize;
            }

            public void setPageSize(Integer pageSize) {
                this.pageSize = pageSize;
            }
        }
    }
}
