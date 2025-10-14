package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentPlanCriteriaTest {

    @Test
    void newPaymentPlanCriteriaHasAllFiltersNullTest() {
        var paymentPlanCriteria = new PaymentPlanCriteria();
        assertThat(paymentPlanCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paymentPlanCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentPlanCriteria = new PaymentPlanCriteria();

        setAllFilters(paymentPlanCriteria);

        assertThat(paymentPlanCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paymentPlanCriteriaCopyCreatesNullFilterTest() {
        var paymentPlanCriteria = new PaymentPlanCriteria();
        var copy = paymentPlanCriteria.copy();

        assertThat(paymentPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentPlanCriteria)
        );
    }

    @Test
    void paymentPlanCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentPlanCriteria = new PaymentPlanCriteria();
        setAllFilters(paymentPlanCriteria);

        var copy = paymentPlanCriteria.copy();

        assertThat(paymentPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paymentPlanCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentPlanCriteria = new PaymentPlanCriteria();

        assertThat(paymentPlanCriteria).hasToString("PaymentPlanCriteria{}");
    }

    private static void setAllFilters(PaymentPlanCriteria paymentPlanCriteria) {
        paymentPlanCriteria.id();
        paymentPlanCriteria.name();
        paymentPlanCriteria.monthsAfterHandover();
        paymentPlanCriteria.propertyId();
        paymentPlanCriteria.distinct();
    }

    private static Condition<PaymentPlanCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getMonthsAfterHandover()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaymentPlanCriteria> copyFiltersAre(PaymentPlanCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getMonthsAfterHandover(), copy.getMonthsAfterHandover()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
