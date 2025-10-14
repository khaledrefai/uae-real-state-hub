package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BillingSubscriptionCriteriaTest {

    @Test
    void newBillingSubscriptionCriteriaHasAllFiltersNullTest() {
        var billingSubscriptionCriteria = new BillingSubscriptionCriteria();
        assertThat(billingSubscriptionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void billingSubscriptionCriteriaFluentMethodsCreatesFiltersTest() {
        var billingSubscriptionCriteria = new BillingSubscriptionCriteria();

        setAllFilters(billingSubscriptionCriteria);

        assertThat(billingSubscriptionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void billingSubscriptionCriteriaCopyCreatesNullFilterTest() {
        var billingSubscriptionCriteria = new BillingSubscriptionCriteria();
        var copy = billingSubscriptionCriteria.copy();

        assertThat(billingSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(billingSubscriptionCriteria)
        );
    }

    @Test
    void billingSubscriptionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var billingSubscriptionCriteria = new BillingSubscriptionCriteria();
        setAllFilters(billingSubscriptionCriteria);

        var copy = billingSubscriptionCriteria.copy();

        assertThat(billingSubscriptionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(billingSubscriptionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var billingSubscriptionCriteria = new BillingSubscriptionCriteria();

        assertThat(billingSubscriptionCriteria).hasToString("BillingSubscriptionCriteria{}");
    }

    private static void setAllFilters(BillingSubscriptionCriteria billingSubscriptionCriteria) {
        billingSubscriptionCriteria.id();
        billingSubscriptionCriteria.status();
        billingSubscriptionCriteria.startDate();
        billingSubscriptionCriteria.endDate();
        billingSubscriptionCriteria.stripeCustomerId();
        billingSubscriptionCriteria.stripeSubscriptionId();
        billingSubscriptionCriteria.cancelAtPeriodEnd();
        billingSubscriptionCriteria.isActive();
        billingSubscriptionCriteria.siteId();
        billingSubscriptionCriteria.distinct();
    }

    private static Condition<BillingSubscriptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getStripeCustomerId()) &&
                condition.apply(criteria.getStripeSubscriptionId()) &&
                condition.apply(criteria.getCancelAtPeriodEnd()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getSiteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BillingSubscriptionCriteria> copyFiltersAre(
        BillingSubscriptionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getStripeCustomerId(), copy.getStripeCustomerId()) &&
                condition.apply(criteria.getStripeSubscriptionId(), copy.getStripeSubscriptionId()) &&
                condition.apply(criteria.getCancelAtPeriodEnd(), copy.getCancelAtPeriodEnd()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getSiteId(), copy.getSiteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
