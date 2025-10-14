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

    // jhipster-needle-application-properties-property

    public External getExternal() {
        return external;
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
}
