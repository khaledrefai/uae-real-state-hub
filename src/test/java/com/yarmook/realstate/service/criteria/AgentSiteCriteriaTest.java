package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgentSiteCriteriaTest {

    @Test
    void newAgentSiteCriteriaHasAllFiltersNullTest() {
        var agentSiteCriteria = new AgentSiteCriteria();
        assertThat(agentSiteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void agentSiteCriteriaFluentMethodsCreatesFiltersTest() {
        var agentSiteCriteria = new AgentSiteCriteria();

        setAllFilters(agentSiteCriteria);

        assertThat(agentSiteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void agentSiteCriteriaCopyCreatesNullFilterTest() {
        var agentSiteCriteria = new AgentSiteCriteria();
        var copy = agentSiteCriteria.copy();

        assertThat(agentSiteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(agentSiteCriteria)
        );
    }

    @Test
    void agentSiteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agentSiteCriteria = new AgentSiteCriteria();
        setAllFilters(agentSiteCriteria);

        var copy = agentSiteCriteria.copy();

        assertThat(agentSiteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(agentSiteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agentSiteCriteria = new AgentSiteCriteria();

        assertThat(agentSiteCriteria).hasToString("AgentSiteCriteria{}");
    }

    private static void setAllFilters(AgentSiteCriteria agentSiteCriteria) {
        agentSiteCriteria.id();
        agentSiteCriteria.slug();
        agentSiteCriteria.displayName();
        agentSiteCriteria.theme();
        agentSiteCriteria.primaryColor();
        agentSiteCriteria.secondaryColor();
        agentSiteCriteria.logoUrl();
        agentSiteCriteria.contactEmail();
        agentSiteCriteria.contactPhone();
        agentSiteCriteria.contactWhatsapp();
        agentSiteCriteria.domain();
        agentSiteCriteria.isActive();
        agentSiteCriteria.ownerId();
        agentSiteCriteria.planId();
        agentSiteCriteria.distinct();
    }

    private static Condition<AgentSiteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getDisplayName()) &&
                condition.apply(criteria.getTheme()) &&
                condition.apply(criteria.getPrimaryColor()) &&
                condition.apply(criteria.getSecondaryColor()) &&
                condition.apply(criteria.getLogoUrl()) &&
                condition.apply(criteria.getContactEmail()) &&
                condition.apply(criteria.getContactPhone()) &&
                condition.apply(criteria.getContactWhatsapp()) &&
                condition.apply(criteria.getDomain()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getPlanId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgentSiteCriteria> copyFiltersAre(AgentSiteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getDisplayName(), copy.getDisplayName()) &&
                condition.apply(criteria.getTheme(), copy.getTheme()) &&
                condition.apply(criteria.getPrimaryColor(), copy.getPrimaryColor()) &&
                condition.apply(criteria.getSecondaryColor(), copy.getSecondaryColor()) &&
                condition.apply(criteria.getLogoUrl(), copy.getLogoUrl()) &&
                condition.apply(criteria.getContactEmail(), copy.getContactEmail()) &&
                condition.apply(criteria.getContactPhone(), copy.getContactPhone()) &&
                condition.apply(criteria.getContactWhatsapp(), copy.getContactWhatsapp()) &&
                condition.apply(criteria.getDomain(), copy.getDomain()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getPlanId(), copy.getPlanId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
