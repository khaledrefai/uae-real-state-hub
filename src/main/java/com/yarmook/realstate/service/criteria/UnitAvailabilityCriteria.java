package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.UnitAvailability} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.UnitAvailabilityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /unit-availabilities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitAvailabilityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter buildingName;

    private DoubleFilter areaFrom;

    private StringFilter areaUnit;

    private StringFilter bedroomType;

    private InstantFilter lastUpdated;

    private StringFilter priceCurrency;

    private BigDecimalFilter priceFrom;

    private BigDecimalFilter priceTo;

    private IntegerFilter unitsAvailable;

    private LongFilter propertyId;

    private Boolean distinct;

    public UnitAvailabilityCriteria() {}

    public UnitAvailabilityCriteria(UnitAvailabilityCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.buildingName = other.optionalBuildingName().map(StringFilter::copy).orElse(null);
        this.areaFrom = other.optionalAreaFrom().map(DoubleFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.bedroomType = other.optionalBedroomType().map(StringFilter::copy).orElse(null);
        this.lastUpdated = other.optionalLastUpdated().map(InstantFilter::copy).orElse(null);
        this.priceCurrency = other.optionalPriceCurrency().map(StringFilter::copy).orElse(null);
        this.priceFrom = other.optionalPriceFrom().map(BigDecimalFilter::copy).orElse(null);
        this.priceTo = other.optionalPriceTo().map(BigDecimalFilter::copy).orElse(null);
        this.unitsAvailable = other.optionalUnitsAvailable().map(IntegerFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UnitAvailabilityCriteria copy() {
        return new UnitAvailabilityCriteria(this);
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

    public StringFilter getBuildingName() {
        return buildingName;
    }

    public Optional<StringFilter> optionalBuildingName() {
        return Optional.ofNullable(buildingName);
    }

    public StringFilter buildingName() {
        if (buildingName == null) {
            setBuildingName(new StringFilter());
        }
        return buildingName;
    }

    public void setBuildingName(StringFilter buildingName) {
        this.buildingName = buildingName;
    }

    public DoubleFilter getAreaFrom() {
        return areaFrom;
    }

    public Optional<DoubleFilter> optionalAreaFrom() {
        return Optional.ofNullable(areaFrom);
    }

    public DoubleFilter areaFrom() {
        if (areaFrom == null) {
            setAreaFrom(new DoubleFilter());
        }
        return areaFrom;
    }

    public void setAreaFrom(DoubleFilter areaFrom) {
        this.areaFrom = areaFrom;
    }

    public StringFilter getAreaUnit() {
        return areaUnit;
    }

    public Optional<StringFilter> optionalAreaUnit() {
        return Optional.ofNullable(areaUnit);
    }

    public StringFilter areaUnit() {
        if (areaUnit == null) {
            setAreaUnit(new StringFilter());
        }
        return areaUnit;
    }

    public void setAreaUnit(StringFilter areaUnit) {
        this.areaUnit = areaUnit;
    }

    public StringFilter getBedroomType() {
        return bedroomType;
    }

    public Optional<StringFilter> optionalBedroomType() {
        return Optional.ofNullable(bedroomType);
    }

    public StringFilter bedroomType() {
        if (bedroomType == null) {
            setBedroomType(new StringFilter());
        }
        return bedroomType;
    }

    public void setBedroomType(StringFilter bedroomType) {
        this.bedroomType = bedroomType;
    }

    public InstantFilter getLastUpdated() {
        return lastUpdated;
    }

    public Optional<InstantFilter> optionalLastUpdated() {
        return Optional.ofNullable(lastUpdated);
    }

    public InstantFilter lastUpdated() {
        if (lastUpdated == null) {
            setLastUpdated(new InstantFilter());
        }
        return lastUpdated;
    }

    public void setLastUpdated(InstantFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public StringFilter getPriceCurrency() {
        return priceCurrency;
    }

    public Optional<StringFilter> optionalPriceCurrency() {
        return Optional.ofNullable(priceCurrency);
    }

    public StringFilter priceCurrency() {
        if (priceCurrency == null) {
            setPriceCurrency(new StringFilter());
        }
        return priceCurrency;
    }

    public void setPriceCurrency(StringFilter priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public BigDecimalFilter getPriceFrom() {
        return priceFrom;
    }

    public Optional<BigDecimalFilter> optionalPriceFrom() {
        return Optional.ofNullable(priceFrom);
    }

    public BigDecimalFilter priceFrom() {
        if (priceFrom == null) {
            setPriceFrom(new BigDecimalFilter());
        }
        return priceFrom;
    }

    public void setPriceFrom(BigDecimalFilter priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimalFilter getPriceTo() {
        return priceTo;
    }

    public Optional<BigDecimalFilter> optionalPriceTo() {
        return Optional.ofNullable(priceTo);
    }

    public BigDecimalFilter priceTo() {
        if (priceTo == null) {
            setPriceTo(new BigDecimalFilter());
        }
        return priceTo;
    }

    public void setPriceTo(BigDecimalFilter priceTo) {
        this.priceTo = priceTo;
    }

    public IntegerFilter getUnitsAvailable() {
        return unitsAvailable;
    }

    public Optional<IntegerFilter> optionalUnitsAvailable() {
        return Optional.ofNullable(unitsAvailable);
    }

    public IntegerFilter unitsAvailable() {
        if (unitsAvailable == null) {
            setUnitsAvailable(new IntegerFilter());
        }
        return unitsAvailable;
    }

    public void setUnitsAvailable(IntegerFilter unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
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
        final UnitAvailabilityCriteria that = (UnitAvailabilityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(buildingName, that.buildingName) &&
            Objects.equals(areaFrom, that.areaFrom) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(bedroomType, that.bedroomType) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(priceCurrency, that.priceCurrency) &&
            Objects.equals(priceFrom, that.priceFrom) &&
            Objects.equals(priceTo, that.priceTo) &&
            Objects.equals(unitsAvailable, that.unitsAvailable) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            buildingName,
            areaFrom,
            areaUnit,
            bedroomType,
            lastUpdated,
            priceCurrency,
            priceFrom,
            priceTo,
            unitsAvailable,
            propertyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitAvailabilityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBuildingName().map(f -> "buildingName=" + f + ", ").orElse("") +
            optionalAreaFrom().map(f -> "areaFrom=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalBedroomType().map(f -> "bedroomType=" + f + ", ").orElse("") +
            optionalLastUpdated().map(f -> "lastUpdated=" + f + ", ").orElse("") +
            optionalPriceCurrency().map(f -> "priceCurrency=" + f + ", ").orElse("") +
            optionalPriceFrom().map(f -> "priceFrom=" + f + ", ").orElse("") +
            optionalPriceTo().map(f -> "priceTo=" + f + ", ").orElse("") +
            optionalUnitsAvailable().map(f -> "unitsAvailable=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
