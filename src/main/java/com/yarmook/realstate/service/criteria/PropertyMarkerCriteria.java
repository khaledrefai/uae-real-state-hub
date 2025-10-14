package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.PropertyMarker} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.PropertyMarkerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /property-markers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyMarkerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter extId;

    private StringFilter area;

    private LocalDateFilter completionDate;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private StringFilter name;

    private StringFilter developer;

    private StringFilter status;

    private StringFilter saleStatus;

    private BigDecimalFilter minPrice;

    private StringFilter coverUrl;

    private LongFilter propertyId;

    private Boolean distinct;

    public PropertyMarkerCriteria() {}

    public PropertyMarkerCriteria(PropertyMarkerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.extId = other.optionalExtId().map(LongFilter::copy).orElse(null);
        this.area = other.optionalArea().map(StringFilter::copy).orElse(null);
        this.completionDate = other.optionalCompletionDate().map(LocalDateFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(DoubleFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(DoubleFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.developer = other.optionalDeveloper().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.saleStatus = other.optionalSaleStatus().map(StringFilter::copy).orElse(null);
        this.minPrice = other.optionalMinPrice().map(BigDecimalFilter::copy).orElse(null);
        this.coverUrl = other.optionalCoverUrl().map(StringFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PropertyMarkerCriteria copy() {
        return new PropertyMarkerCriteria(this);
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

    public LongFilter getExtId() {
        return extId;
    }

    public Optional<LongFilter> optionalExtId() {
        return Optional.ofNullable(extId);
    }

    public LongFilter extId() {
        if (extId == null) {
            setExtId(new LongFilter());
        }
        return extId;
    }

    public void setExtId(LongFilter extId) {
        this.extId = extId;
    }

    public StringFilter getArea() {
        return area;
    }

    public Optional<StringFilter> optionalArea() {
        return Optional.ofNullable(area);
    }

    public StringFilter area() {
        if (area == null) {
            setArea(new StringFilter());
        }
        return area;
    }

    public void setArea(StringFilter area) {
        this.area = area;
    }

    public LocalDateFilter getCompletionDate() {
        return completionDate;
    }

    public Optional<LocalDateFilter> optionalCompletionDate() {
        return Optional.ofNullable(completionDate);
    }

    public LocalDateFilter completionDate() {
        if (completionDate == null) {
            setCompletionDate(new LocalDateFilter());
        }
        return completionDate;
    }

    public void setCompletionDate(LocalDateFilter completionDate) {
        this.completionDate = completionDate;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public Optional<DoubleFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            setLatitude(new DoubleFilter());
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public Optional<DoubleFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            setLongitude(new DoubleFilter());
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDeveloper() {
        return developer;
    }

    public Optional<StringFilter> optionalDeveloper() {
        return Optional.ofNullable(developer);
    }

    public StringFilter developer() {
        if (developer == null) {
            setDeveloper(new StringFilter());
        }
        return developer;
    }

    public void setDeveloper(StringFilter developer) {
        this.developer = developer;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getSaleStatus() {
        return saleStatus;
    }

    public Optional<StringFilter> optionalSaleStatus() {
        return Optional.ofNullable(saleStatus);
    }

    public StringFilter saleStatus() {
        if (saleStatus == null) {
            setSaleStatus(new StringFilter());
        }
        return saleStatus;
    }

    public void setSaleStatus(StringFilter saleStatus) {
        this.saleStatus = saleStatus;
    }

    public BigDecimalFilter getMinPrice() {
        return minPrice;
    }

    public Optional<BigDecimalFilter> optionalMinPrice() {
        return Optional.ofNullable(minPrice);
    }

    public BigDecimalFilter minPrice() {
        if (minPrice == null) {
            setMinPrice(new BigDecimalFilter());
        }
        return minPrice;
    }

    public void setMinPrice(BigDecimalFilter minPrice) {
        this.minPrice = minPrice;
    }

    public StringFilter getCoverUrl() {
        return coverUrl;
    }

    public Optional<StringFilter> optionalCoverUrl() {
        return Optional.ofNullable(coverUrl);
    }

    public StringFilter coverUrl() {
        if (coverUrl == null) {
            setCoverUrl(new StringFilter());
        }
        return coverUrl;
    }

    public void setCoverUrl(StringFilter coverUrl) {
        this.coverUrl = coverUrl;
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
        final PropertyMarkerCriteria that = (PropertyMarkerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(extId, that.extId) &&
            Objects.equals(area, that.area) &&
            Objects.equals(completionDate, that.completionDate) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(name, that.name) &&
            Objects.equals(developer, that.developer) &&
            Objects.equals(status, that.status) &&
            Objects.equals(saleStatus, that.saleStatus) &&
            Objects.equals(minPrice, that.minPrice) &&
            Objects.equals(coverUrl, that.coverUrl) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            extId,
            area,
            completionDate,
            latitude,
            longitude,
            name,
            developer,
            status,
            saleStatus,
            minPrice,
            coverUrl,
            propertyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyMarkerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalExtId().map(f -> "extId=" + f + ", ").orElse("") +
            optionalArea().map(f -> "area=" + f + ", ").orElse("") +
            optionalCompletionDate().map(f -> "completionDate=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDeveloper().map(f -> "developer=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSaleStatus().map(f -> "saleStatus=" + f + ", ").orElse("") +
            optionalMinPrice().map(f -> "minPrice=" + f + ", ").orElse("") +
            optionalCoverUrl().map(f -> "coverUrl=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
