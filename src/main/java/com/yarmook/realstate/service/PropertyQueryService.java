package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.service.criteria.PropertyCriteria;
import com.yarmook.realstate.service.dto.PropertyDTO;
import com.yarmook.realstate.service.mapper.PropertyMapper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.RangeFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * MongoDB-backed query service for {@link Property}.
 */
@Service
public class PropertyQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyQueryService.class);

    private final PropertyMapper propertyMapper;
    private final MongoTemplate mongoTemplate;

    public PropertyQueryService(PropertyMapper propertyMapper, MongoTemplate mongoTemplate) {
        this.propertyMapper = propertyMapper;
        this.mongoTemplate = mongoTemplate;
    }

    public Page<PropertyDTO> findByCriteria(PropertyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        Query query = buildQuery(criteria);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Property.class);
        query.with(page);
        List<PropertyDTO> content = mongoTemplate.find(query, Property.class).stream().map(propertyMapper::toDto).toList();
        return new PageImpl<>(content, page, total);
    }

    public long countByCriteria(PropertyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        Query query = buildQuery(criteria);
        return mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Property.class);
    }

    private Query buildQuery(PropertyCriteria criteria) {
        Query query = new Query();
        if (criteria == null) {
            return query;
        }

        List<Criteria> predicates = new ArrayList<>();

        addRangeFilter(predicates, "id", criteria.getId());
        addRangeFilter(predicates, "extId", criteria.getExtId());
        addStringFilter(predicates, "slug", criteria.getSlug());
        addStringFilter(predicates, "name", criteria.getName());
        addStringFilter(predicates, "developer", criteria.getDeveloper());
        addStringFilter(predicates, "area", criteria.getArea());
        addStringFilter(predicates, "city", criteria.getCity());
        addStringFilter(predicates, "country", criteria.getCountry());
        addFilter(predicates, "listingType", criteria.getListingType());
        addFilter(predicates, "status", criteria.getStatus());
        addStringFilter(predicates, "saleStatus", criteria.getSaleStatus());
        addStringFilter(predicates, "readiness", criteria.getReadiness());
        addStringFilter(predicates, "serviceCharge", criteria.getServiceCharge());
        addStringFilter(predicates, "furnishing", criteria.getFurnishing());
        addFilter(predicates, "hasEscrow", criteria.getHasEscrow());
        addFilter(predicates, "postHandover", criteria.getPostHandover());
        addRangeFilter(predicates, "completionDateTime", criteria.getCompletionDateTime());
        addRangeFilter(predicates, "minPrice", criteria.getMinPrice());
        addRangeFilter(predicates, "maxPrice", criteria.getMaxPrice());
        addRangeFilter(predicates, "minPriceAed", criteria.getMinPriceAed());
        addStringFilter(predicates, "priceCurrency", criteria.getPriceCurrency());
        addRangeFilter(predicates, "minArea", criteria.getMinArea());
        addRangeFilter(predicates, "maxArea", criteria.getMaxArea());
        addStringFilter(predicates, "areaUnit", criteria.getAreaUnit());
        addRangeFilter(predicates, "latitude", criteria.getLatitude());
        addRangeFilter(predicates, "longitude", criteria.getLongitude());
        addStringFilter(predicates, "coverUrl", criteria.getCoverUrl());
        addStringFilter(predicates, "videoUrl", criteria.getVideoUrl());
        addStringFilter(predicates, "brochureUrl", criteria.getBrochureUrl());
        addStringFilter(predicates, "layoutsPdfUrl", criteria.getLayoutsPdfUrl());
        addStringFilter(predicates, "website", criteria.getWebsite());
        addRangeFilter(predicates, "publishedAt", criteria.getPublishedAt());
        addRangeFilter(predicates, "updatedAt", criteria.getUpdatedAt());
        addMarkerCriteria(predicates, criteria.getMarkerId());

        if (!predicates.isEmpty()) {
            if (predicates.size() == 1) {
                query.addCriteria(predicates.get(0));
            } else {
                query.addCriteria(new Criteria().andOperator(predicates.toArray(new Criteria[0])));
            }
        }

        return query;
    }

    private void addMarkerCriteria(List<Criteria> predicates, RangeFilter<Long> filter) {
        if (filter == null) {
            return;
        }
        addEqualityFilter(predicates, "marker.$id", filter);
        Criteria rangeCriteria = Criteria.where("marker.$id");
        boolean hasRange = false;
        if (filter.getGreaterThan() != null) {
            rangeCriteria.gt(filter.getGreaterThan());
            hasRange = true;
        }
        if (filter.getGreaterThanOrEqual() != null) {
            rangeCriteria.gte(filter.getGreaterThanOrEqual());
            hasRange = true;
        }
        if (filter.getLessThan() != null) {
            rangeCriteria.lt(filter.getLessThan());
            hasRange = true;
        }
        if (filter.getLessThanOrEqual() != null) {
            rangeCriteria.lte(filter.getLessThanOrEqual());
            hasRange = true;
        }
        if (hasRange) {
            predicates.add(rangeCriteria);
        }
    }

    private <T> void addFilter(List<Criteria> predicates, String field, Filter<T> filter) {
        if (filter == null) {
            return;
        }
        addEqualityFilter(predicates, field, filter);
    }

    private <T extends Comparable<? super T>> void addRangeFilter(List<Criteria> predicates, String field, RangeFilter<T> filter) {
        if (filter == null) {
            return;
        }
        addEqualityFilter(predicates, field, filter);

        Criteria rangeCriteria = Criteria.where(field);
        boolean hasRange = false;
        if (filter.getGreaterThan() != null) {
            rangeCriteria.gt(filter.getGreaterThan());
            hasRange = true;
        }
        if (filter.getGreaterThanOrEqual() != null) {
            rangeCriteria.gte(filter.getGreaterThanOrEqual());
            hasRange = true;
        }
        if (filter.getLessThan() != null) {
            rangeCriteria.lt(filter.getLessThan());
            hasRange = true;
        }
        if (filter.getLessThanOrEqual() != null) {
            rangeCriteria.lte(filter.getLessThanOrEqual());
            hasRange = true;
        }
        if (hasRange) {
            predicates.add(rangeCriteria);
        }
    }

    private void addStringFilter(List<Criteria> predicates, String field, StringFilter filter) {
        if (filter == null) {
            return;
        }

        Boolean specified = filter.getSpecified();
        if (specified != null) {
            predicates.add(specified ? Criteria.where(field).ne(null) : Criteria.where(field).is(null));
        }

        boolean ignoreCase = Boolean.TRUE.equals(invokeFilterMethod(filter, "getIgnoreCase"));
        if (filter.getEquals() != null) {
            predicates.add(
                ignoreCase
                    ? regexCriteria(field, "^" + Pattern.quote(filter.getEquals()) + "$", true)
                    : Criteria.where(field).is(filter.getEquals())
            );
        }
        if (filter.getNotEquals() != null) {
            predicates.add(
                ignoreCase
                    ? negatedRegexCriteria(field, "^" + Pattern.quote(filter.getNotEquals()) + "$", true)
                    : Criteria.where(field).ne(filter.getNotEquals())
            );
        }
        if (hasValues(filter.getIn())) {
            if (ignoreCase) {
                Criteria[] options = filter
                    .getIn()
                    .stream()
                    .map(value -> regexCriteria(field, "^" + Pattern.quote(value) + "$", true))
                    .toArray(Criteria[]::new);
                predicates.add(new Criteria().orOperator(options));
            } else {
                predicates.add(Criteria.where(field).in(filter.getIn()));
            }
        }
        if (hasValues(filter.getNotIn())) {
            if (ignoreCase) {
                Criteria[] options = filter
                    .getNotIn()
                    .stream()
                    .map(value -> regexCriteria(field, "^" + Pattern.quote(value) + "$", true))
                    .toArray(Criteria[]::new);
                predicates.add(new Criteria().norOperator(options));
            } else {
                predicates.add(Criteria.where(field).nin(filter.getNotIn()));
            }
        }
        if (filter.getContains() != null) {
            predicates.add(regexCriteria(field, Pattern.quote(filter.getContains()), ignoreCase));
        }
        if (filter.getDoesNotContain() != null) {
            predicates.add(negatedRegexCriteria(field, Pattern.quote(filter.getDoesNotContain()), ignoreCase));
        }
        String startsWith = invokeFilterMethod(filter, "getStartsWith");
        if (startsWith != null) {
            predicates.add(regexCriteria(field, "^" + Pattern.quote(startsWith), ignoreCase));
        }
        String endsWith = invokeFilterMethod(filter, "getEndsWith");
        if (endsWith != null) {
            predicates.add(regexCriteria(field, Pattern.quote(endsWith) + "$", ignoreCase));
        }
    }

    private <T> void addEqualityFilter(List<Criteria> predicates, String field, Filter<T> filter) {
        if (filter == null) {
            return;
        }

        Boolean specified = filter.getSpecified();
        if (specified != null) {
            predicates.add(specified ? Criteria.where(field).ne(null) : Criteria.where(field).is(null));
        }
        if (filter.getEquals() != null) {
            predicates.add(Criteria.where(field).is(filter.getEquals()));
        }
        if (filter.getNotEquals() != null) {
            predicates.add(Criteria.where(field).ne(filter.getNotEquals()));
        }
        if (hasValues(filter.getIn())) {
            predicates.add(Criteria.where(field).in(filter.getIn()));
        }
        if (hasValues(filter.getNotIn())) {
            predicates.add(Criteria.where(field).nin(filter.getNotIn()));
        }
    }

    private boolean hasValues(Collection<?> values) {
        return values != null && !values.isEmpty();
    }

    private Criteria regexCriteria(String field, String pattern, boolean ignoreCase) {
        Pattern compiled = ignoreCase ? Pattern.compile(pattern, Pattern.CASE_INSENSITIVE) : Pattern.compile(pattern);
        return Criteria.where(field).regex(compiled);
    }

    private Criteria negatedRegexCriteria(String field, String pattern, boolean ignoreCase) {
        Pattern compiled = ignoreCase ? Pattern.compile(pattern, Pattern.CASE_INSENSITIVE) : Pattern.compile(pattern);
        return Criteria.where(field).not().regex(compiled);
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeFilterMethod(StringFilter filter, String methodName) {
        if (filter == null) {
            return null;
        }
        try {
            Method method = filter.getClass().getMethod(methodName);
            return (T) method.invoke(filter);
        } catch (NoSuchMethodException ignored) {
            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to invoke " + methodName + " on " + filter.getClass(), e);
        }
    }
}
