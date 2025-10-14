package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContactLeadCriteriaTest {

    @Test
    void newContactLeadCriteriaHasAllFiltersNullTest() {
        var contactLeadCriteria = new ContactLeadCriteria();
        assertThat(contactLeadCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contactLeadCriteriaFluentMethodsCreatesFiltersTest() {
        var contactLeadCriteria = new ContactLeadCriteria();

        setAllFilters(contactLeadCriteria);

        assertThat(contactLeadCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contactLeadCriteriaCopyCreatesNullFilterTest() {
        var contactLeadCriteria = new ContactLeadCriteria();
        var copy = contactLeadCriteria.copy();

        assertThat(contactLeadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contactLeadCriteria)
        );
    }

    @Test
    void contactLeadCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contactLeadCriteria = new ContactLeadCriteria();
        setAllFilters(contactLeadCriteria);

        var copy = contactLeadCriteria.copy();

        assertThat(contactLeadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contactLeadCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contactLeadCriteria = new ContactLeadCriteria();

        assertThat(contactLeadCriteria).hasToString("ContactLeadCriteria{}");
    }

    private static void setAllFilters(ContactLeadCriteria contactLeadCriteria) {
        contactLeadCriteria.id();
        contactLeadCriteria.name();
        contactLeadCriteria.email();
        contactLeadCriteria.phone();
        contactLeadCriteria.whatsapp();
        contactLeadCriteria.message();
        contactLeadCriteria.source();
        contactLeadCriteria.createdAt();
        contactLeadCriteria.status();
        contactLeadCriteria.siteId();
        contactLeadCriteria.propertyId();
        contactLeadCriteria.distinct();
    }

    private static Condition<ContactLeadCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getWhatsapp()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSiteId()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContactLeadCriteria> copyFiltersAre(ContactLeadCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getWhatsapp(), copy.getWhatsapp()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSiteId(), copy.getSiteId()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
