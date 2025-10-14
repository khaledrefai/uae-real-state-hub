package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.UnitBlock} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.UnitBlockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /unit-blocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitBlockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter normalizedType;

    private StringFilter unitType;

    private StringFilter bedroomsAmount;

    private StringFilter unitBedrooms;

    private StringFilter areaUnit;

    private IntegerFilter unitsAmount;

    private DoubleFilter unitsAreaFrom;

    private DoubleFilter unitsAreaTo;

    private StringFilter unitsAreaFromM2;

    private StringFilter unitsAreaToM2;

    private BigDecimalFilter unitsPriceFrom;

    private BigDecimalFilter unitsPriceTo;

    private StringFilter priceCurrency;

    private StringFilter typicalImageUrl;

    private LongFilter propertyId;

    private Boolean distinct;

    public UnitBlockCriteria() {}

    public UnitBlockCriteria(UnitBlockCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.normalizedType = other.optionalNormalizedType().map(StringFilter::copy).orElse(null);
        this.unitType = other.optionalUnitType().map(StringFilter::copy).orElse(null);
        this.bedroomsAmount = other.optionalBedroomsAmount().map(StringFilter::copy).orElse(null);
        this.unitBedrooms = other.optionalUnitBedrooms().map(StringFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.unitsAmount = other.optionalUnitsAmount().map(IntegerFilter::copy).orElse(null);
        this.unitsAreaFrom = other.optionalUnitsAreaFrom().map(DoubleFilter::copy).orElse(null);
        this.unitsAreaTo = other.optionalUnitsAreaTo().map(DoubleFilter::copy).orElse(null);
        this.unitsAreaFromM2 = other.optionalUnitsAreaFromM2().map(StringFilter::copy).orElse(null);
        this.unitsAreaToM2 = other.optionalUnitsAreaToM2().map(StringFilter::copy).orElse(null);
        this.unitsPriceFrom = other.optionalUnitsPriceFrom().map(BigDecimalFilter::copy).orElse(null);
        this.unitsPriceTo = other.optionalUnitsPriceTo().map(BigDecimalFilter::copy).orElse(null);
        this.priceCurrency = other.optionalPriceCurrency().map(StringFilter::copy).orElse(null);
        this.typicalImageUrl = other.optionalTypicalImageUrl().map(StringFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UnitBlockCriteria copy() {
        return new UnitBlockCriteria(this);
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

    public StringFilter getNormalizedType() {
        return normalizedType;
    }

    public Optional<StringFilter> optionalNormalizedType() {
        return Optional.ofNullable(normalizedType);
    }

    public StringFilter normalizedType() {
        if (normalizedType == null) {
            setNormalizedType(new StringFilter());
        }
        return normalizedType;
    }

    public void setNormalizedType(StringFilter normalizedType) {
        this.normalizedType = normalizedType;
    }

    public StringFilter getUnitType() {
        return unitType;
    }

    public Optional<StringFilter> optionalUnitType() {
        return Optional.ofNullable(unitType);
    }

    public StringFilter unitType() {
        if (unitType == null) {
            setUnitType(new StringFilter());
        }
        return unitType;
    }

    public void setUnitType(StringFilter unitType) {
        this.unitType = unitType;
    }

    public StringFilter getBedroomsAmount() {
        return bedroomsAmount;
    }

    public Optional<StringFilter> optionalBedroomsAmount() {
        return Optional.ofNullable(bedroomsAmount);
    }

    public StringFilter bedroomsAmount() {
        if (bedroomsAmount == null) {
            setBedroomsAmount(new StringFilter());
        }
        return bedroomsAmount;
    }

    public void setBedroomsAmount(StringFilter bedroomsAmount) {
        this.bedroomsAmount = bedroomsAmount;
    }

    public StringFilter getUnitBedrooms() {
        return unitBedrooms;
    }

    public Optional<StringFilter> optionalUnitBedrooms() {
        return Optional.ofNullable(unitBedrooms);
    }

    public StringFilter unitBedrooms() {
        if (unitBedrooms == null) {
            setUnitBedrooms(new StringFilter());
        }
        return unitBedrooms;
    }

    public void setUnitBedrooms(StringFilter unitBedrooms) {
        this.unitBedrooms = unitBedrooms;
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

    public IntegerFilter getUnitsAmount() {
        return unitsAmount;
    }

    public Optional<IntegerFilter> optionalUnitsAmount() {
        return Optional.ofNullable(unitsAmount);
    }

    public IntegerFilter unitsAmount() {
        if (unitsAmount == null) {
            setUnitsAmount(new IntegerFilter());
        }
        return unitsAmount;
    }

    public void setUnitsAmount(IntegerFilter unitsAmount) {
        this.unitsAmount = unitsAmount;
    }

    public DoubleFilter getUnitsAreaFrom() {
        return unitsAreaFrom;
    }

    public Optional<DoubleFilter> optionalUnitsAreaFrom() {
        return Optional.ofNullable(unitsAreaFrom);
    }

    public DoubleFilter unitsAreaFrom() {
        if (unitsAreaFrom == null) {
            setUnitsAreaFrom(new DoubleFilter());
        }
        return unitsAreaFrom;
    }

    public void setUnitsAreaFrom(DoubleFilter unitsAreaFrom) {
        this.unitsAreaFrom = unitsAreaFrom;
    }

    public DoubleFilter getUnitsAreaTo() {
        return unitsAreaTo;
    }

    public Optional<DoubleFilter> optionalUnitsAreaTo() {
        return Optional.ofNullable(unitsAreaTo);
    }

    public DoubleFilter unitsAreaTo() {
        if (unitsAreaTo == null) {
            setUnitsAreaTo(new DoubleFilter());
        }
        return unitsAreaTo;
    }

    public void setUnitsAreaTo(DoubleFilter unitsAreaTo) {
        this.unitsAreaTo = unitsAreaTo;
    }

    public StringFilter getUnitsAreaFromM2() {
        return unitsAreaFromM2;
    }

    public Optional<StringFilter> optionalUnitsAreaFromM2() {
        return Optional.ofNullable(unitsAreaFromM2);
    }

    public StringFilter unitsAreaFromM2() {
        if (unitsAreaFromM2 == null) {
            setUnitsAreaFromM2(new StringFilter());
        }
        return unitsAreaFromM2;
    }

    public void setUnitsAreaFromM2(StringFilter unitsAreaFromM2) {
        this.unitsAreaFromM2 = unitsAreaFromM2;
    }

    public StringFilter getUnitsAreaToM2() {
        return unitsAreaToM2;
    }

    public Optional<StringFilter> optionalUnitsAreaToM2() {
        return Optional.ofNullable(unitsAreaToM2);
    }

    public StringFilter unitsAreaToM2() {
        if (unitsAreaToM2 == null) {
            setUnitsAreaToM2(new StringFilter());
        }
        return unitsAreaToM2;
    }

    public void setUnitsAreaToM2(StringFilter unitsAreaToM2) {
        this.unitsAreaToM2 = unitsAreaToM2;
    }

    public BigDecimalFilter getUnitsPriceFrom() {
        return unitsPriceFrom;
    }

    public Optional<BigDecimalFilter> optionalUnitsPriceFrom() {
        return Optional.ofNullable(unitsPriceFrom);
    }

    public BigDecimalFilter unitsPriceFrom() {
        if (unitsPriceFrom == null) {
            setUnitsPriceFrom(new BigDecimalFilter());
        }
        return unitsPriceFrom;
    }

    public void setUnitsPriceFrom(BigDecimalFilter unitsPriceFrom) {
        this.unitsPriceFrom = unitsPriceFrom;
    }

    public BigDecimalFilter getUnitsPriceTo() {
        return unitsPriceTo;
    }

    public Optional<BigDecimalFilter> optionalUnitsPriceTo() {
        return Optional.ofNullable(unitsPriceTo);
    }

    public BigDecimalFilter unitsPriceTo() {
        if (unitsPriceTo == null) {
            setUnitsPriceTo(new BigDecimalFilter());
        }
        return unitsPriceTo;
    }

    public void setUnitsPriceTo(BigDecimalFilter unitsPriceTo) {
        this.unitsPriceTo = unitsPriceTo;
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

    public StringFilter getTypicalImageUrl() {
        return typicalImageUrl;
    }

    public Optional<StringFilter> optionalTypicalImageUrl() {
        return Optional.ofNullable(typicalImageUrl);
    }

    public StringFilter typicalImageUrl() {
        if (typicalImageUrl == null) {
            setTypicalImageUrl(new StringFilter());
        }
        return typicalImageUrl;
    }

    public void setTypicalImageUrl(StringFilter typicalImageUrl) {
        this.typicalImageUrl = typicalImageUrl;
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
        final UnitBlockCriteria that = (UnitBlockCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(normalizedType, that.normalizedType) &&
            Objects.equals(unitType, that.unitType) &&
            Objects.equals(bedroomsAmount, that.bedroomsAmount) &&
            Objects.equals(unitBedrooms, that.unitBedrooms) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(unitsAmount, that.unitsAmount) &&
            Objects.equals(unitsAreaFrom, that.unitsAreaFrom) &&
            Objects.equals(unitsAreaTo, that.unitsAreaTo) &&
            Objects.equals(unitsAreaFromM2, that.unitsAreaFromM2) &&
            Objects.equals(unitsAreaToM2, that.unitsAreaToM2) &&
            Objects.equals(unitsPriceFrom, that.unitsPriceFrom) &&
            Objects.equals(unitsPriceTo, that.unitsPriceTo) &&
            Objects.equals(priceCurrency, that.priceCurrency) &&
            Objects.equals(typicalImageUrl, that.typicalImageUrl) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            normalizedType,
            unitType,
            bedroomsAmount,
            unitBedrooms,
            areaUnit,
            unitsAmount,
            unitsAreaFrom,
            unitsAreaTo,
            unitsAreaFromM2,
            unitsAreaToM2,
            unitsPriceFrom,
            unitsPriceTo,
            priceCurrency,
            typicalImageUrl,
            propertyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitBlockCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNormalizedType().map(f -> "normalizedType=" + f + ", ").orElse("") +
            optionalUnitType().map(f -> "unitType=" + f + ", ").orElse("") +
            optionalBedroomsAmount().map(f -> "bedroomsAmount=" + f + ", ").orElse("") +
            optionalUnitBedrooms().map(f -> "unitBedrooms=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalUnitsAmount().map(f -> "unitsAmount=" + f + ", ").orElse("") +
            optionalUnitsAreaFrom().map(f -> "unitsAreaFrom=" + f + ", ").orElse("") +
            optionalUnitsAreaTo().map(f -> "unitsAreaTo=" + f + ", ").orElse("") +
            optionalUnitsAreaFromM2().map(f -> "unitsAreaFromM2=" + f + ", ").orElse("") +
            optionalUnitsAreaToM2().map(f -> "unitsAreaToM2=" + f + ", ").orElse("") +
            optionalUnitsPriceFrom().map(f -> "unitsPriceFrom=" + f + ", ").orElse("") +
            optionalUnitsPriceTo().map(f -> "unitsPriceTo=" + f + ", ").orElse("") +
            optionalPriceCurrency().map(f -> "priceCurrency=" + f + ", ").orElse("") +
            optionalTypicalImageUrl().map(f -> "typicalImageUrl=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
