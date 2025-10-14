package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UnitBlockCriteriaTest {

    @Test
    void newUnitBlockCriteriaHasAllFiltersNullTest() {
        var unitBlockCriteria = new UnitBlockCriteria();
        assertThat(unitBlockCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void unitBlockCriteriaFluentMethodsCreatesFiltersTest() {
        var unitBlockCriteria = new UnitBlockCriteria();

        setAllFilters(unitBlockCriteria);

        assertThat(unitBlockCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void unitBlockCriteriaCopyCreatesNullFilterTest() {
        var unitBlockCriteria = new UnitBlockCriteria();
        var copy = unitBlockCriteria.copy();

        assertThat(unitBlockCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(unitBlockCriteria)
        );
    }

    @Test
    void unitBlockCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var unitBlockCriteria = new UnitBlockCriteria();
        setAllFilters(unitBlockCriteria);

        var copy = unitBlockCriteria.copy();

        assertThat(unitBlockCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(unitBlockCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var unitBlockCriteria = new UnitBlockCriteria();

        assertThat(unitBlockCriteria).hasToString("UnitBlockCriteria{}");
    }

    private static void setAllFilters(UnitBlockCriteria unitBlockCriteria) {
        unitBlockCriteria.id();
        unitBlockCriteria.normalizedType();
        unitBlockCriteria.unitType();
        unitBlockCriteria.bedroomsAmount();
        unitBlockCriteria.unitBedrooms();
        unitBlockCriteria.areaUnit();
        unitBlockCriteria.unitsAmount();
        unitBlockCriteria.unitsAreaFrom();
        unitBlockCriteria.unitsAreaTo();
        unitBlockCriteria.unitsAreaFromM2();
        unitBlockCriteria.unitsAreaToM2();
        unitBlockCriteria.unitsPriceFrom();
        unitBlockCriteria.unitsPriceTo();
        unitBlockCriteria.priceCurrency();
        unitBlockCriteria.typicalImageUrl();
        unitBlockCriteria.propertyId();
        unitBlockCriteria.distinct();
    }

    private static Condition<UnitBlockCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNormalizedType()) &&
                condition.apply(criteria.getUnitType()) &&
                condition.apply(criteria.getBedroomsAmount()) &&
                condition.apply(criteria.getUnitBedrooms()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getUnitsAmount()) &&
                condition.apply(criteria.getUnitsAreaFrom()) &&
                condition.apply(criteria.getUnitsAreaTo()) &&
                condition.apply(criteria.getUnitsAreaFromM2()) &&
                condition.apply(criteria.getUnitsAreaToM2()) &&
                condition.apply(criteria.getUnitsPriceFrom()) &&
                condition.apply(criteria.getUnitsPriceTo()) &&
                condition.apply(criteria.getPriceCurrency()) &&
                condition.apply(criteria.getTypicalImageUrl()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UnitBlockCriteria> copyFiltersAre(UnitBlockCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNormalizedType(), copy.getNormalizedType()) &&
                condition.apply(criteria.getUnitType(), copy.getUnitType()) &&
                condition.apply(criteria.getBedroomsAmount(), copy.getBedroomsAmount()) &&
                condition.apply(criteria.getUnitBedrooms(), copy.getUnitBedrooms()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getUnitsAmount(), copy.getUnitsAmount()) &&
                condition.apply(criteria.getUnitsAreaFrom(), copy.getUnitsAreaFrom()) &&
                condition.apply(criteria.getUnitsAreaTo(), copy.getUnitsAreaTo()) &&
                condition.apply(criteria.getUnitsAreaFromM2(), copy.getUnitsAreaFromM2()) &&
                condition.apply(criteria.getUnitsAreaToM2(), copy.getUnitsAreaToM2()) &&
                condition.apply(criteria.getUnitsPriceFrom(), copy.getUnitsPriceFrom()) &&
                condition.apply(criteria.getUnitsPriceTo(), copy.getUnitsPriceTo()) &&
                condition.apply(criteria.getPriceCurrency(), copy.getPriceCurrency()) &&
                condition.apply(criteria.getTypicalImageUrl(), copy.getTypicalImageUrl()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
