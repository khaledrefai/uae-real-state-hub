package com.yarmook.realstate.service.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.RangeFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Helper utilities to evaluate JHipster {@link Filter} instances against in-memory values.
 * <p>
 * These are used by the Mongo query services after switching away from JPA Specifications.
 */
public final class FilterMatcher {

    private FilterMatcher() {}

    public static boolean matches(StringFilter filter, String value) {
        if (!matches((Filter<String>) filter, value)) {
            return false;
        }
        if (filter == null || value == null) {
            return true;
        }
        boolean ignoreCase = Boolean.TRUE.equals(invoke(filter, "getIgnoreCase"));
        String candidate = ignoreCase ? value.toLowerCase(Locale.ROOT) : value;

        String contains = invoke(filter, "getContains");
        if (contains != null) {
            String needle = ignoreCase ? contains.toLowerCase(Locale.ROOT) : contains;
            if (!candidate.contains(needle)) {
                return false;
            }
        }

        String doesNotContain = invoke(filter, "getDoesNotContain");
        if (doesNotContain != null) {
            String needle = ignoreCase ? doesNotContain.toLowerCase(Locale.ROOT) : doesNotContain;
            if (candidate.contains(needle)) {
                return false;
            }
        }

        String startsWith = invoke(filter, "getStartsWith");
        if (startsWith != null) {
            String prefix = ignoreCase ? startsWith.toLowerCase(Locale.ROOT) : startsWith;
            if (!candidate.startsWith(prefix)) {
                return false;
            }
        }

        String endsWith = invoke(filter, "getEndsWith");
        if (endsWith != null) {
            String suffix = ignoreCase ? endsWith.toLowerCase(Locale.ROOT) : endsWith;
            if (!candidate.endsWith(suffix)) {
                return false;
            }
        }

        return true;
    }

    public static <T extends Comparable<? super T>> boolean matchesRange(RangeFilter<T> filter, T value) {
        if (!matches((Filter<T>) filter, value)) {
            return false;
        }
        if (filter == null) {
            return true;
        }

        T greaterThan = invoke(filter, "getGreaterThan");
        T greaterThanOrEqual = invoke(filter, "getGreaterThanOrEqual");
        T lessThan = invoke(filter, "getLessThan");
        T lessThanOrEqual = invoke(filter, "getLessThanOrEqual");

        if (value == null) {
            return greaterThan == null && greaterThanOrEqual == null && lessThan == null && lessThanOrEqual == null;
        }

        if (greaterThan != null && value.compareTo(greaterThan) <= 0) {
            return false;
        }

        if (greaterThanOrEqual != null && value.compareTo(greaterThanOrEqual) < 0) {
            return false;
        }

        if (lessThan != null && value.compareTo(lessThan) >= 0) {
            return false;
        }

        if (lessThanOrEqual != null && value.compareTo(lessThanOrEqual) > 0) {
            return false;
        }

        return true;
    }

    public static <T> boolean matches(Filter<T> filter, T value) {
        if (filter == null) {
            return true;
        }

        Boolean specified = invoke(filter, "getSpecified");
        if (specified != null) {
            if (specified && value == null) {
                return false;
            }
            if (!specified && value != null) {
                return false;
            }
        }

        T equalsValue = invoke(filter, "getEquals");
        if (equalsValue != null && !Objects.equals(value, equalsValue)) {
            return false;
        }

        T notEquals = invoke(filter, "getNotEquals");
        if (notEquals != null && Objects.equals(value, notEquals)) {
            return false;
        }

        Collection<T> in = invoke(filter, "getIn");
        if (in != null && !in.isEmpty()) {
            if (value == null || !in.contains(value)) {
                return false;
            }
        }

        Collection<T> notIn = invoke(filter, "getNotIn");
        if (notIn != null && !notIn.isEmpty() && value != null && notIn.contains(value)) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private static <R> R invoke(Object target, String methodName) {
        if (target == null) {
            return null;
        }
        try {
            Method method = target.getClass().getMethod(methodName);
            return (R) method.invoke(target);
        } catch (NoSuchMethodException ignored) {
            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to invoke " + methodName + " on " + target.getClass(), e);
        }
    }
}
