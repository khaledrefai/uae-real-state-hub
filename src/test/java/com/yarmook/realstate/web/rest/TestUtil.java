package com.yarmook.realstate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

public final class TestUtil {

    private TestUtil() {}

    public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        assertThat(domainObject1.toString()).isNotNull();
        assertThat(domainObject1).isEqualTo(domainObject1);
        assertThat(domainObject1).hasSameHashCodeAs(domainObject1);
        T domainObject2 = clazz.getConstructor().newInstance();
        assertThat(domainObject1).isNotEqualTo(domainObject2);
        assertThat(domainObject1).hasSameHashCodeAs(domainObject2);
    }
}
