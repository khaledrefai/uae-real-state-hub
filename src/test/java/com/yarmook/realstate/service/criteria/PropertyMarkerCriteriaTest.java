package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PropertyMarkerCriteriaTest {

    @Test
    void newPropertyMarkerCriteriaHasAllFiltersNullTest() {
        var propertyMarkerCriteria = new PropertyMarkerCriteria();
        assertThat(propertyMarkerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void propertyMarkerCriteriaFluentMethodsCreatesFiltersTest() {
        var propertyMarkerCriteria = new PropertyMarkerCriteria();

        setAllFilters(propertyMarkerCriteria);

        assertThat(propertyMarkerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void propertyMarkerCriteriaCopyCreatesNullFilterTest() {
        var propertyMarkerCriteria = new PropertyMarkerCriteria();
        var copy = propertyMarkerCriteria.copy();

        assertThat(propertyMarkerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(propertyMarkerCriteria)
        );
    }

    @Test
    void propertyMarkerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var propertyMarkerCriteria = new PropertyMarkerCriteria();
        setAllFilters(propertyMarkerCriteria);

        var copy = propertyMarkerCriteria.copy();

        assertThat(propertyMarkerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(propertyMarkerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var propertyMarkerCriteria = new PropertyMarkerCriteria();

        assertThat(propertyMarkerCriteria).hasToString("PropertyMarkerCriteria{}");
    }

    private static void setAllFilters(PropertyMarkerCriteria propertyMarkerCriteria) {
        propertyMarkerCriteria.id();
        propertyMarkerCriteria.extId();
        propertyMarkerCriteria.area();
        propertyMarkerCriteria.completionDate();
        propertyMarkerCriteria.latitude();
        propertyMarkerCriteria.longitude();
        propertyMarkerCriteria.name();
        propertyMarkerCriteria.developer();
        propertyMarkerCriteria.status();
        propertyMarkerCriteria.saleStatus();
        propertyMarkerCriteria.minPrice();
        propertyMarkerCriteria.coverUrl();
        propertyMarkerCriteria.propertyId();
        propertyMarkerCriteria.distinct();
    }

    private static Condition<PropertyMarkerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getExtId()) &&
                condition.apply(criteria.getArea()) &&
                condition.apply(criteria.getCompletionDate()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDeveloper()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSaleStatus()) &&
                condition.apply(criteria.getMinPrice()) &&
                condition.apply(criteria.getCoverUrl()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PropertyMarkerCriteria> copyFiltersAre(
        PropertyMarkerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getExtId(), copy.getExtId()) &&
                condition.apply(criteria.getArea(), copy.getArea()) &&
                condition.apply(criteria.getCompletionDate(), copy.getCompletionDate()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDeveloper(), copy.getDeveloper()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSaleStatus(), copy.getSaleStatus()) &&
                condition.apply(criteria.getMinPrice(), copy.getMinPrice()) &&
                condition.apply(criteria.getCoverUrl(), copy.getCoverUrl()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
