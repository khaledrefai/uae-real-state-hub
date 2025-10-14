package com.yarmook.realstate.service.criteria;

import com.yarmook.realstate.domain.enumeration.MediaKind;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.MediaAsset} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.MediaAssetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /media-assets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaAssetCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MediaKind
     */
    public static class MediaKindFilter extends Filter<MediaKind> {

        public MediaKindFilter() {}

        public MediaKindFilter(MediaKindFilter filter) {
            super(filter);
        }

        @Override
        public MediaKindFilter copy() {
            return new MediaKindFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private MediaKindFilter kind;

    private StringFilter url;

    private StringFilter mimeType;

    private IntegerFilter width;

    private IntegerFilter height;

    private LongFilter mediaSize;

    private StringFilter source;

    private StringFilter title;

    private StringFilter altText;

    private IntegerFilter sortOrder;

    private LongFilter propertyId;

    private Boolean distinct;

    public MediaAssetCriteria() {}

    public MediaAssetCriteria(MediaAssetCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.kind = other.optionalKind().map(MediaKindFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.mimeType = other.optionalMimeType().map(StringFilter::copy).orElse(null);
        this.width = other.optionalWidth().map(IntegerFilter::copy).orElse(null);
        this.height = other.optionalHeight().map(IntegerFilter::copy).orElse(null);
        this.mediaSize = other.optionalMediaSize().map(LongFilter::copy).orElse(null);
        this.source = other.optionalSource().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.altText = other.optionalAltText().map(StringFilter::copy).orElse(null);
        this.sortOrder = other.optionalSortOrder().map(IntegerFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MediaAssetCriteria copy() {
        return new MediaAssetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public MediaKindFilter getKind() {
        return kind;
    }

    public Optional<MediaKindFilter> optionalKind() {
        return Optional.ofNullable(kind);
    }

    public MediaKindFilter kind() {
        if (kind == null) {
            setKind(new MediaKindFilter());
        }
        return kind;
    }

    public void setKind(MediaKindFilter kind) {
        this.kind = kind;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getMimeType() {
        return mimeType;
    }

    public Optional<StringFilter> optionalMimeType() {
        return Optional.ofNullable(mimeType);
    }

    public StringFilter mimeType() {
        if (mimeType == null) {
            setMimeType(new StringFilter());
        }
        return mimeType;
    }

    public void setMimeType(StringFilter mimeType) {
        this.mimeType = mimeType;
    }

    public IntegerFilter getWidth() {
        return width;
    }

    public Optional<IntegerFilter> optionalWidth() {
        return Optional.ofNullable(width);
    }

    public IntegerFilter width() {
        if (width == null) {
            setWidth(new IntegerFilter());
        }
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public Optional<IntegerFilter> optionalHeight() {
        return Optional.ofNullable(height);
    }

    public IntegerFilter height() {
        if (height == null) {
            setHeight(new IntegerFilter());
        }
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public LongFilter getMediaSize() {
        return mediaSize;
    }

    public Optional<LongFilter> optionalMediaSize() {
        return Optional.ofNullable(mediaSize);
    }

    public LongFilter mediaSize() {
        if (mediaSize == null) {
            setMediaSize(new LongFilter());
        }
        return mediaSize;
    }

    public void setMediaSize(LongFilter mediaSize) {
        this.mediaSize = mediaSize;
    }

    public StringFilter getSource() {
        return source;
    }

    public Optional<StringFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public StringFilter source() {
        if (source == null) {
            setSource(new StringFilter());
        }
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getAltText() {
        return altText;
    }

    public Optional<StringFilter> optionalAltText() {
        return Optional.ofNullable(altText);
    }

    public StringFilter altText() {
        if (altText == null) {
            setAltText(new StringFilter());
        }
        return altText;
    }

    public void setAltText(StringFilter altText) {
        this.altText = altText;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public Optional<IntegerFilter> optionalSortOrder() {
        return Optional.ofNullable(sortOrder);
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            setSortOrder(new IntegerFilter());
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LongFilter getPropertyId() {
        return propertyId;
    }

    public Optional<LongFilter> optionalPropertyId() {
        return Optional.ofNullable(propertyId);
    }

    public LongFilter propertyId() {
        if (propertyId == null) {
            setPropertyId(new LongFilter());
        }
        return propertyId;
    }

    public void setPropertyId(LongFilter propertyId) {
        this.propertyId = propertyId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MediaAssetCriteria that = (MediaAssetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(kind, that.kind) &&
            Objects.equals(url, that.url) &&
            Objects.equals(mimeType, that.mimeType) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(mediaSize, that.mediaSize) &&
            Objects.equals(source, that.source) &&
            Objects.equals(title, that.title) &&
            Objects.equals(altText, that.altText) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kind, url, mimeType, width, height, mediaSize, source, title, altText, sortOrder, propertyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaAssetCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalKind().map(f -> "kind=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalMimeType().map(f -> "mimeType=" + f + ", ").orElse("") +
            optionalWidth().map(f -> "width=" + f + ", ").orElse("") +
            optionalHeight().map(f -> "height=" + f + ", ").orElse("") +
            optionalMediaSize().map(f -> "mediaSize=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalAltText().map(f -> "altText=" + f + ", ").orElse("") +
            optionalSortOrder().map(f -> "sortOrder=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
