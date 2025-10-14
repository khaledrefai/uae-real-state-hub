package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PropertyCriteriaTest {

    @Test
    void newPropertyCriteriaHasAllFiltersNullTest() {
        var propertyCriteria = new PropertyCriteria();
        assertThat(propertyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void propertyCriteriaFluentMethodsCreatesFiltersTest() {
        var propertyCriteria = new PropertyCriteria();

        setAllFilters(propertyCriteria);

        assertThat(propertyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void propertyCriteriaCopyCreatesNullFilterTest() {
        var propertyCriteria = new PropertyCriteria();
        var copy = propertyCriteria.copy();

        assertThat(propertyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(propertyCriteria)
        );
    }

    @Test
    void propertyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var propertyCriteria = new PropertyCriteria();
        setAllFilters(propertyCriteria);

        var copy = propertyCriteria.copy();

        assertThat(propertyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(propertyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var propertyCriteria = new PropertyCriteria();

        assertThat(propertyCriteria).hasToString("PropertyCriteria{}");
    }

    private static void setAllFilters(PropertyCriteria propertyCriteria) {
        propertyCriteria.id();
        propertyCriteria.extId();
        propertyCriteria.slug();
        propertyCriteria.name();
        propertyCriteria.developer();
        propertyCriteria.area();
        propertyCriteria.city();
        propertyCriteria.country();
        propertyCriteria.listingType();
        propertyCriteria.status();
        propertyCriteria.saleStatus();
        propertyCriteria.readiness();
        propertyCriteria.serviceCharge();
        propertyCriteria.furnishing();
        propertyCriteria.hasEscrow();
        propertyCriteria.postHandover();
        propertyCriteria.completionDateTime();
        propertyCriteria.minPrice();
        propertyCriteria.maxPrice();
        propertyCriteria.minPriceAed();
        propertyCriteria.priceCurrency();
        propertyCriteria.minArea();
        propertyCriteria.maxArea();
        propertyCriteria.areaUnit();
        propertyCriteria.latitude();
        propertyCriteria.longitude();
        propertyCriteria.coverUrl();
        propertyCriteria.videoUrl();
        propertyCriteria.brochureUrl();
        propertyCriteria.layoutsPdfUrl();
        propertyCriteria.website();
        propertyCriteria.publishedAt();
        propertyCriteria.updatedAt();
        propertyCriteria.markerId();
        propertyCriteria.distinct();
    }

    private static Condition<PropertyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getExtId()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDeveloper()) &&
                condition.apply(criteria.getArea()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getCountry()) &&
                condition.apply(criteria.getListingType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSaleStatus()) &&
                condition.apply(criteria.getReadiness()) &&
                condition.apply(criteria.getServiceCharge()) &&
                condition.apply(criteria.getFurnishing()) &&
                condition.apply(criteria.getHasEscrow()) &&
                condition.apply(criteria.getPostHandover()) &&
                condition.apply(criteria.getCompletionDateTime()) &&
                condition.apply(criteria.getMinPrice()) &&
                condition.apply(criteria.getMaxPrice()) &&
                condition.apply(criteria.getMinPriceAed()) &&
                condition.apply(criteria.getPriceCurrency()) &&
                condition.apply(criteria.getMinArea()) &&
                condition.apply(criteria.getMaxArea()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getCoverUrl()) &&
                condition.apply(criteria.getVideoUrl()) &&
                condition.apply(criteria.getBrochureUrl()) &&
                condition.apply(criteria.getLayoutsPdfUrl()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getPublishedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getMarkerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PropertyCriteria> copyFiltersAre(PropertyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getExtId(), copy.getExtId()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDeveloper(), copy.getDeveloper()) &&
                condition.apply(criteria.getArea(), copy.getArea()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getCountry(), copy.getCountry()) &&
                condition.apply(criteria.getListingType(), copy.getListingType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSaleStatus(), copy.getSaleStatus()) &&
                condition.apply(criteria.getReadiness(), copy.getReadiness()) &&
                condition.apply(criteria.getServiceCharge(), copy.getServiceCharge()) &&
                condition.apply(criteria.getFurnishing(), copy.getFurnishing()) &&
                condition.apply(criteria.getHasEscrow(), copy.getHasEscrow()) &&
                condition.apply(criteria.getPostHandover(), copy.getPostHandover()) &&
                condition.apply(criteria.getCompletionDateTime(), copy.getCompletionDateTime()) &&
                condition.apply(criteria.getMinPrice(), copy.getMinPrice()) &&
                condition.apply(criteria.getMaxPrice(), copy.getMaxPrice()) &&
                condition.apply(criteria.getMinPriceAed(), copy.getMinPriceAed()) &&
                condition.apply(criteria.getPriceCurrency(), copy.getPriceCurrency()) &&
                condition.apply(criteria.getMinArea(), copy.getMinArea()) &&
                condition.apply(criteria.getMaxArea(), copy.getMaxArea()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getCoverUrl(), copy.getCoverUrl()) &&
                condition.apply(criteria.getVideoUrl(), copy.getVideoUrl()) &&
                condition.apply(criteria.getBrochureUrl(), copy.getBrochureUrl()) &&
                condition.apply(criteria.getLayoutsPdfUrl(), copy.getLayoutsPdfUrl()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getPublishedAt(), copy.getPublishedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getMarkerId(), copy.getMarkerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
