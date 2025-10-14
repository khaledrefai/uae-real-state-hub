package com.yarmook.realstate.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MediaAssetCriteriaTest {

    @Test
    void newMediaAssetCriteriaHasAllFiltersNullTest() {
        var mediaAssetCriteria = new MediaAssetCriteria();
        assertThat(mediaAssetCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void mediaAssetCriteriaFluentMethodsCreatesFiltersTest() {
        var mediaAssetCriteria = new MediaAssetCriteria();

        setAllFilters(mediaAssetCriteria);

        assertThat(mediaAssetCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void mediaAssetCriteriaCopyCreatesNullFilterTest() {
        var mediaAssetCriteria = new MediaAssetCriteria();
        var copy = mediaAssetCriteria.copy();

        assertThat(mediaAssetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(mediaAssetCriteria)
        );
    }

    @Test
    void mediaAssetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mediaAssetCriteria = new MediaAssetCriteria();
        setAllFilters(mediaAssetCriteria);

        var copy = mediaAssetCriteria.copy();

        assertThat(mediaAssetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(mediaAssetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mediaAssetCriteria = new MediaAssetCriteria();

        assertThat(mediaAssetCriteria).hasToString("MediaAssetCriteria{}");
    }

    private static void setAllFilters(MediaAssetCriteria mediaAssetCriteria) {
        mediaAssetCriteria.id();
        mediaAssetCriteria.kind();
        mediaAssetCriteria.url();
        mediaAssetCriteria.mimeType();
        mediaAssetCriteria.width();
        mediaAssetCriteria.height();
        mediaAssetCriteria.mediaSize();
        mediaAssetCriteria.source();
        mediaAssetCriteria.title();
        mediaAssetCriteria.altText();
        mediaAssetCriteria.sortOrder();
        mediaAssetCriteria.propertyId();
        mediaAssetCriteria.distinct();
    }

    private static Condition<MediaAssetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKind()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getMimeType()) &&
                condition.apply(criteria.getWidth()) &&
                condition.apply(criteria.getHeight()) &&
                condition.apply(criteria.getMediaSize()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getAltText()) &&
                condition.apply(criteria.getSortOrder()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MediaAssetCriteria> copyFiltersAre(MediaAssetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKind(), copy.getKind()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getMimeType(), copy.getMimeType()) &&
                condition.apply(criteria.getWidth(), copy.getWidth()) &&
                condition.apply(criteria.getHeight(), copy.getHeight()) &&
                condition.apply(criteria.getMediaSize(), copy.getMediaSize()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getAltText(), copy.getAltText()) &&
                condition.apply(criteria.getSortOrder(), copy.getSortOrder()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
