package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FacilityCriteriaTest {

    @Test
    void newFacilityCriteriaHasAllFiltersNullTest() {
        var facilityCriteria = new FacilityCriteria();
        assertThat(facilityCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void facilityCriteriaFluentMethodsCreatesFiltersTest() {
        var facilityCriteria = new FacilityCriteria();

        setAllFilters(facilityCriteria);

        assertThat(facilityCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void facilityCriteriaCopyCreatesNullFilterTest() {
        var facilityCriteria = new FacilityCriteria();
        var copy = facilityCriteria.copy();

        assertThat(facilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityCriteria)
        );
    }

    @Test
    void facilityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var facilityCriteria = new FacilityCriteria();
        setAllFilters(facilityCriteria);

        var copy = facilityCriteria.copy();

        assertThat(facilityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(facilityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var facilityCriteria = new FacilityCriteria();

        assertThat(facilityCriteria).hasToString("FacilityCriteria{}");
    }

    private static void setAllFilters(FacilityCriteria facilityCriteria) {
        facilityCriteria.id();
        facilityCriteria.name();
        facilityCriteria.source();
        facilityCriteria.imageUrl();
        facilityCriteria.propertyId();
        facilityCriteria.distinct();
    }

    private static Condition<FacilityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FacilityCriteria> copyFiltersAre(FacilityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
