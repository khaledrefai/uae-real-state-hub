package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UnitAvailabilityCriteriaTest {

    @Test
    void newUnitAvailabilityCriteriaHasAllFiltersNullTest() {
        var unitAvailabilityCriteria = new UnitAvailabilityCriteria();
        assertThat(unitAvailabilityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void unitAvailabilityCriteriaFluentMethodsCreatesFiltersTest() {
        var unitAvailabilityCriteria = new UnitAvailabilityCriteria();

        setAllFilters(unitAvailabilityCriteria);

        assertThat(unitAvailabilityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void unitAvailabilityCriteriaCopyCreatesNullFilterTest() {
        var unitAvailabilityCriteria = new UnitAvailabilityCriteria();
        var copy = unitAvailabilityCriteria.copy();

        assertThat(unitAvailabilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(unitAvailabilityCriteria)
        );
    }

    @Test
    void unitAvailabilityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var unitAvailabilityCriteria = new UnitAvailabilityCriteria();
        setAllFilters(unitAvailabilityCriteria);

        var copy = unitAvailabilityCriteria.copy();

        assertThat(unitAvailabilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(unitAvailabilityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var unitAvailabilityCriteria = new UnitAvailabilityCriteria();

        assertThat(unitAvailabilityCriteria).hasToString("UnitAvailabilityCriteria{}");
    }

    private static void setAllFilters(UnitAvailabilityCriteria unitAvailabilityCriteria) {
        unitAvailabilityCriteria.id();
        unitAvailabilityCriteria.buildingName();
        unitAvailabilityCriteria.areaFrom();
        unitAvailabilityCriteria.areaUnit();
        unitAvailabilityCriteria.bedroomType();
        unitAvailabilityCriteria.lastUpdated();
        unitAvailabilityCriteria.priceCurrency();
        unitAvailabilityCriteria.priceFrom();
        unitAvailabilityCriteria.priceTo();
        unitAvailabilityCriteria.unitsAvailable();
        unitAvailabilityCriteria.propertyId();
        unitAvailabilityCriteria.distinct();
    }

    private static Condition<UnitAvailabilityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBuildingName()) &&
                condition.apply(criteria.getAreaFrom()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getBedroomType()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getPriceCurrency()) &&
                condition.apply(criteria.getPriceFrom()) &&
                condition.apply(criteria.getPriceTo()) &&
                condition.apply(criteria.getUnitsAvailable()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UnitAvailabilityCriteria> copyFiltersAre(
        UnitAvailabilityCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBuildingName(), copy.getBuildingName()) &&
                condition.apply(criteria.getAreaFrom(), copy.getAreaFrom()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getBedroomType(), copy.getBedroomType()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getPriceCurrency(), copy.getPriceCurrency()) &&
                condition.apply(criteria.getPriceFrom(), copy.getPriceFrom()) &&
                condition.apply(criteria.getPriceTo(), copy.getPriceTo()) &&
                condition.apply(criteria.getUnitsAvailable(), copy.getUnitsAvailable()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
