package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgentProfileCriteriaTest {

    @Test
    void newAgentProfileCriteriaHasAllFiltersNullTest() {
        var agentProfileCriteria = new AgentProfileCriteria();
        assertThat(agentProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void agentProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var agentProfileCriteria = new AgentProfileCriteria();

        setAllFilters(agentProfileCriteria);

        assertThat(agentProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void agentProfileCriteriaCopyCreatesNullFilterTest() {
        var agentProfileCriteria = new AgentProfileCriteria();
        var copy = agentProfileCriteria.copy();

        assertThat(agentProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(agentProfileCriteria)
        );
    }

    @Test
    void agentProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agentProfileCriteria = new AgentProfileCriteria();
        setAllFilters(agentProfileCriteria);

        var copy = agentProfileCriteria.copy();

        assertThat(agentProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(agentProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agentProfileCriteria = new AgentProfileCriteria();

        assertThat(agentProfileCriteria).hasToString("AgentProfileCriteria{}");
    }

    private static void setAllFilters(AgentProfileCriteria agentProfileCriteria) {
        agentProfileCriteria.id();
        agentProfileCriteria.externalUserId();
        agentProfileCriteria.fullName();
        agentProfileCriteria.companyName();
        agentProfileCriteria.email();
        agentProfileCriteria.phone();
        agentProfileCriteria.whatsapp();
        agentProfileCriteria.country();
        agentProfileCriteria.city();
        agentProfileCriteria.website();
        agentProfileCriteria.active();
        agentProfileCriteria.distinct();
    }

    private static Condition<AgentProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getExternalUserId()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getCompanyName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getWhatsapp()) &&
                condition.apply(criteria.getCountry()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgentProfileCriteria> copyFiltersAre(
        AgentProfileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getExternalUserId(), copy.getExternalUserId()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getCompanyName(), copy.getCompanyName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getWhatsapp(), copy.getWhatsapp()) &&
                condition.apply(criteria.getCountry(), copy.getCountry()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
