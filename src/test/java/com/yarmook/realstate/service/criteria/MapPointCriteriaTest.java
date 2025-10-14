package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MapPointCriteriaTest {

    @Test
    void newMapPointCriteriaHasAllFiltersNullTest() {
        var mapPointCriteria = new MapPointCriteria();
        assertThat(mapPointCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void mapPointCriteriaFluentMethodsCreatesFiltersTest() {
        var mapPointCriteria = new MapPointCriteria();

        setAllFilters(mapPointCriteria);

        assertThat(mapPointCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void mapPointCriteriaCopyCreatesNullFilterTest() {
        var mapPointCriteria = new MapPointCriteria();
        var copy = mapPointCriteria.copy();

        assertThat(mapPointCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(mapPointCriteria)
        );
    }

    @Test
    void mapPointCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mapPointCriteria = new MapPointCriteria();
        setAllFilters(mapPointCriteria);

        var copy = mapPointCriteria.copy();

        assertThat(mapPointCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(mapPointCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mapPointCriteria = new MapPointCriteria();

        assertThat(mapPointCriteria).hasToString("MapPointCriteria{}");
    }

    private static void setAllFilters(MapPointCriteria mapPointCriteria) {
        mapPointCriteria.id();
        mapPointCriteria.name();
        mapPointCriteria.distanceKm();
        mapPointCriteria.propertyId();
        mapPointCriteria.distinct();
    }

    private static Condition<MapPointCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDistanceKm()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MapPointCriteria> copyFiltersAre(MapPointCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistanceKm(), copy.getDistanceKm()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
