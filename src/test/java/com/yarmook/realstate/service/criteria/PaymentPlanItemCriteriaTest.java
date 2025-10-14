package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentPlanItemCriteriaTest {

    @Test
    void newPaymentPlanItemCriteriaHasAllFiltersNullTest() {
        var paymentPlanItemCriteria = new PaymentPlanItemCriteria();
        assertThat(paymentPlanItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentPlanItemCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentPlanItemCriteria = new PaymentPlanItemCriteria();

        setAllFilters(paymentPlanItemCriteria);

        assertThat(paymentPlanItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentPlanItemCriteriaCopyCreatesNullFilterTest() {
        var paymentPlanItemCriteria = new PaymentPlanItemCriteria();
        var copy = paymentPlanItemCriteria.copy();

        assertThat(paymentPlanItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentPlanItemCriteria)
        );
    }

    @Test
    void paymentPlanItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentPlanItemCriteria = new PaymentPlanItemCriteria();
        setAllFilters(paymentPlanItemCriteria);

        var copy = paymentPlanItemCriteria.copy();

        assertThat(paymentPlanItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentPlanItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentPlanItemCriteria = new PaymentPlanItemCriteria();

        assertThat(paymentPlanItemCriteria).hasToString("PaymentPlanItemCriteria{}");
    }

    private static void setAllFilters(PaymentPlanItemCriteria paymentPlanItemCriteria) {
        paymentPlanItemCriteria.id();
        paymentPlanItemCriteria.orderNo();
        paymentPlanItemCriteria.paymentTime();
        paymentPlanItemCriteria.percentOfPayment();
        paymentPlanItemCriteria.planId();
        paymentPlanItemCriteria.distinct();
    }

    private static Condition<PaymentPlanItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderNo()) &&
                condition.apply(criteria.getPaymentTime()) &&
                condition.apply(criteria.getPercentOfPayment()) &&
                condition.apply(criteria.getPlanId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentPlanItemCriteria> copyFiltersAre(
        PaymentPlanItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderNo(), copy.getOrderNo()) &&
                condition.apply(criteria.getPaymentTime(), copy.getPaymentTime()) &&
                condition.apply(criteria.getPercentOfPayment(), copy.getPercentOfPayment()) &&
                condition.apply(criteria.getPlanId(), copy.getPlanId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
