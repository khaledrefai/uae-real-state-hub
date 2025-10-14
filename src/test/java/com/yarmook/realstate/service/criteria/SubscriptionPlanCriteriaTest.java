package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SubscriptionPlanCriteriaTest {

    @Test
    void newSubscriptionPlanCriteriaHasAllFiltersNullTest() {
        var subscriptionPlanCriteria = new SubscriptionPlanCriteria();
        assertThat(subscriptionPlanCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void subscriptionPlanCriteriaFluentMethodsCreatesFiltersTest() {
        var subscriptionPlanCriteria = new SubscriptionPlanCriteria();

        setAllFilters(subscriptionPlanCriteria);

        assertThat(subscriptionPlanCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void subscriptionPlanCriteriaCopyCreatesNullFilterTest() {
        var subscriptionPlanCriteria = new SubscriptionPlanCriteria();
        var copy = subscriptionPlanCriteria.copy();

        assertThat(subscriptionPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(subscriptionPlanCriteria)
        );
    }

    @Test
    void subscriptionPlanCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var subscriptionPlanCriteria = new SubscriptionPlanCriteria();
        setAllFilters(subscriptionPlanCriteria);

        var copy = subscriptionPlanCriteria.copy();

        assertThat(subscriptionPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(subscriptionPlanCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var subscriptionPlanCriteria = new SubscriptionPlanCriteria();

        assertThat(subscriptionPlanCriteria).hasToString("SubscriptionPlanCriteria{}");
    }

    private static void setAllFilters(SubscriptionPlanCriteria subscriptionPlanCriteria) {
        subscriptionPlanCriteria.id();
        subscriptionPlanCriteria.code();
        subscriptionPlanCriteria.name();
        subscriptionPlanCriteria.priceMonthly();
        subscriptionPlanCriteria.priceYearly();
        subscriptionPlanCriteria.currency();
        subscriptionPlanCriteria.stripePriceMonthlyId();
        subscriptionPlanCriteria.stripePriceYearlyId();
        subscriptionPlanCriteria.maxListings();
        subscriptionPlanCriteria.distinct();
    }

    private static Condition<SubscriptionPlanCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPriceMonthly()) &&
                condition.apply(criteria.getPriceYearly()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getStripePriceMonthlyId()) &&
                condition.apply(criteria.getStripePriceYearlyId()) &&
                condition.apply(criteria.getMaxListings()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SubscriptionPlanCriteria> copyFiltersAre(
        SubscriptionPlanCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPriceMonthly(), copy.getPriceMonthly()) &&
                condition.apply(criteria.getPriceYearly(), copy.getPriceYearly()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getStripePriceMonthlyId(), copy.getStripePriceMonthlyId()) &&
                condition.apply(criteria.getStripePriceYearlyId(), copy.getStripePriceYearlyId()) &&
                condition.apply(criteria.getMaxListings(), copy.getMaxListings()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
