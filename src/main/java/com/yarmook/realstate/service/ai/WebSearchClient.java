package com.yarmook.realstate.service.ai;

import com.yarmook.realstate.config.ApplicationProperties;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WebSearchClient {

    private static final Logger LOG = LoggerFactory.getLogger(WebSearchClient.class);

    private static final Pattern RESULT_LINK_PATTERN = Pattern.compile(
        "(?is)<a\\s+[^>]*href=\"(?<href>[^\"]+)\"[^>]*class=['\"]result-link['\"][^>]*>(?<title>.*?)</a>"
    );
    private static final Pattern RESULT_SNIPPET_PATTERN = Pattern.compile(
        "(?is)<td\\s+class=['\"]result-snippet['\"]>\\s*(?<snippet>.*?)\\s*</td>"
    );
    private static final Pattern RESULT_DISPLAY_URL_PATTERN = Pattern.compile(
        "(?is)<span\\s+class=['\"]link-text['\"]>\\s*(?<url>.*?)\\s*</span>"
    );
    private static final Pattern TAG_PATTERN = Pattern.compile("(?is)<[^>]+>");

    private final ApplicationProperties.Ai.WebSearch webSearchProperties;
    private final RestTemplate restTemplate;

    public WebSearchClient(RestTemplateBuilder restTemplateBuilder, ApplicationProperties applicationProperties) {
        this.webSearchProperties = applicationProperties.getAi().getWebSearch();

        int timeoutSeconds = Math.max(1, webSearchProperties.getTimeoutSeconds() != null ? webSearchProperties.getTimeoutSeconds() : 8);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().set(HttpHeaders.USER_AGENT, Objects.requireNonNullElse(webSearchProperties.getUserAgent(), "Mozilla/5.0"));
            request.getHeaders().setAccept(List.of(MediaType.TEXT_HTML, MediaType.ALL));
            return execution.execute(request, body);
        });

        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(timeoutSeconds))
            .setReadTimeout(Duration.ofSeconds(timeoutSeconds))
            .additionalInterceptors(interceptors)
            .build();
    }

    public boolean isEnabled() {
        return webSearchProperties.isEnabled() && StringUtils.hasText(webSearchProperties.getBaseUrl());
    }

    public List<WebSearchResult> search(String query) {
        Integer configuredLimit = webSearchProperties.getMaxResults();
        int limit = configuredLimit != null ? configuredLimit : 5;
        return search(query, limit);
    }

    public List<WebSearchResult> search(String query, int limit) {
        if (!isEnabled() || !StringUtils.hasText(query) || limit <= 0) {
            return Collections.emptyList();
        }

        int maxResults = Math.max(
            1,
            Math.min(limit, webSearchProperties.getMaxResults() != null ? webSearchProperties.getMaxResults() : limit)
        );
        try {
            String html = restTemplate.getForObject(buildSearchUri(query), String.class);
            if (!StringUtils.hasText(html)) {
                return Collections.emptyList();
            }
            List<WebSearchResult> results = parseDuckDuckGoLiteResults(html, maxResults);
            LOG.info("Live web search returned {} results for query: {}", results.size(), query);
            return results;
        } catch (RestClientException ex) {
            LOG.warn("Live web search failed for query '{}': {}", query, ex.getMessage());
            return Collections.emptyList();
        } catch (Exception ex) {
            LOG.warn("Unexpected error during live web search for query '{}': {}", query, ex.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildSearchUri(String query) {
        String baseUrl = webSearchProperties.getBaseUrl();
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(normalizedBaseUrl)
            .queryParam("q", query)
            .queryParam("kl", Objects.requireNonNullElse(webSearchProperties.getRegion(), "wt-wt"));
        return builder.build().encode().toUriString();
    }

    private List<WebSearchResult> parseDuckDuckGoLiteResults(String html, int maxResults) {
        Matcher linkMatcher = RESULT_LINK_PATTERN.matcher(html);
        List<LinkHit> hits = new ArrayList<>();
        while (linkMatcher.find()) {
            hits.add(new LinkHit(linkMatcher.start(), linkMatcher.end(), linkMatcher.group("href"), linkMatcher.group("title")));
        }

        if (hits.isEmpty()) {
            return Collections.emptyList();
        }

        List<WebSearchResult> results = new ArrayList<>();
        for (int i = 0; i < hits.size() && results.size() < maxResults; i++) {
            LinkHit hit = hits.get(i);
            int blockEnd = i + 1 < hits.size() ? hits.get(i + 1).start() : html.length();
            String block = html.substring(hit.end(), Math.max(hit.end(), blockEnd));

            String url = normalizeUrl(resolveDuckDuckGoRedirect(hit.href()));
            if (!StringUtils.hasText(url)) {
                continue;
            }

            String title = cleanHtmlText(hit.title());
            String snippet = extractFirst(block, RESULT_SNIPPET_PATTERN, "snippet");
            String displayUrl = extractFirst(block, RESULT_DISPLAY_URL_PATTERN, "url");
            if (!StringUtils.hasText(displayUrl)) {
                displayUrl = deriveDisplayUrl(url);
            }

            results.add(new WebSearchResult(title, url, snippet, displayUrl));
        }

        return results;
    }

    private String extractFirst(String block, Pattern pattern, String groupName) {
        if (!StringUtils.hasText(block)) {
            return null;
        }
        Matcher matcher = pattern.matcher(block);
        if (!matcher.find()) {
            return null;
        }
        return cleanHtmlText(matcher.group(groupName));
    }

    private String cleanHtmlText(String html) {
        if (!StringUtils.hasText(html)) {
            return null;
        }
        String noTags = TAG_PATTERN.matcher(html).replaceAll(" ");
        String decoded = HtmlUtils.htmlUnescape(noTags);
        String normalized = decoded.replace('\u00A0', ' ').replaceAll("\\s+", " ").trim();
        return StringUtils.hasText(normalized) ? normalized : null;
    }

    private String resolveDuckDuckGoRedirect(String rawHref) {
        if (!StringUtils.hasText(rawHref)) {
            return null;
        }
        String href = HtmlUtils.htmlUnescape(rawHref.trim());
        if (href.startsWith("//")) {
            href = "https:" + href;
        }

        int uddgIndex = href.toLowerCase(Locale.ENGLISH).indexOf("uddg=");
        if (uddgIndex >= 0) {
            String encoded = href.substring(uddgIndex + 5);
            int ampIndex = encoded.indexOf('&');
            if (ampIndex >= 0) {
                encoded = encoded.substring(0, ampIndex);
            }
            try {
                return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
            } catch (Exception ignored) {
                return encoded;
            }
        }
        return href;
    }

    private String normalizeUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }
        String normalized = url.trim();
        if (normalized.startsWith("//")) {
            normalized = "https:" + normalized;
        }
        if (!normalized.startsWith("http://") && !normalized.startsWith("https://")) {
            return null;
        }
        return normalized;
    }

    private String deriveDisplayUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }
        String normalized = url.replaceFirst("^https?://", "");
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }

    private record LinkHit(int start, int end, String href, String title) {}

    public record WebSearchResult(String title, String url, String snippet, String displayUrl) {}
}
