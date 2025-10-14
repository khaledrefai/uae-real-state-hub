package com.yarmook.realstate.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.UnitBlock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitBlockDTO implements Serializable {

    private Long id;

    private String normalizedType;

    private String unitType;

    private String bedroomsAmount;

    private String unitBedrooms;

    private String areaUnit;

    private Integer unitsAmount;

    private Double unitsAreaFrom;

    private Double unitsAreaTo;

    private String unitsAreaFromM2;

    private String unitsAreaToM2;

    private BigDecimal unitsPriceFrom;

    private BigDecimal unitsPriceTo;

    private String priceCurrency;

    private String typicalImageUrl;

    private PropertyDTO property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalizedType() {
        return normalizedType;
    }

    public void setNormalizedType(String normalizedType) {
        this.normalizedType = normalizedType;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getBedroomsAmount() {
        return bedroomsAmount;
    }

    public void setBedroomsAmount(String bedroomsAmount) {
        this.bedroomsAmount = bedroomsAmount;
    }

    public String getUnitBedrooms() {
        return unitBedrooms;
    }

    public void setUnitBedrooms(String unitBedrooms) {
        this.unitBedrooms = unitBedrooms;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public Integer getUnitsAmount() {
        return unitsAmount;
    }

    public void setUnitsAmount(Integer unitsAmount) {
        this.unitsAmount = unitsAmount;
    }

    public Double getUnitsAreaFrom() {
        return unitsAreaFrom;
    }

    public void setUnitsAreaFrom(Double unitsAreaFrom) {
        this.unitsAreaFrom = unitsAreaFrom;
    }

    public Double getUnitsAreaTo() {
        return unitsAreaTo;
    }

    public void setUnitsAreaTo(Double unitsAreaTo) {
        this.unitsAreaTo = unitsAreaTo;
    }

    public String getUnitsAreaFromM2() {
        return unitsAreaFromM2;
    }

    public void setUnitsAreaFromM2(String unitsAreaFromM2) {
        this.unitsAreaFromM2 = unitsAreaFromM2;
    }

    public String getUnitsAreaToM2() {
        return unitsAreaToM2;
    }

    public void setUnitsAreaToM2(String unitsAreaToM2) {
        this.unitsAreaToM2 = unitsAreaToM2;
    }

    public BigDecimal getUnitsPriceFrom() {
        return unitsPriceFrom;
    }

    public void setUnitsPriceFrom(BigDecimal unitsPriceFrom) {
        this.unitsPriceFrom = unitsPriceFrom;
    }

    public BigDecimal getUnitsPriceTo() {
        return unitsPriceTo;
    }

    public void setUnitsPriceTo(BigDecimal unitsPriceTo) {
        this.unitsPriceTo = unitsPriceTo;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getTypicalImageUrl() {
        return typicalImageUrl;
    }

    public void setTypicalImageUrl(String typicalImageUrl) {
        this.typicalImageUrl = typicalImageUrl;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitBlockDTO)) {
            return false;
        }

        UnitBlockDTO unitBlockDTO = (UnitBlockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitBlockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitBlockDTO{" +
            "id=" + getId() +
            ", normalizedType='" + getNormalizedType() + "'" +
            ", unitType='" + getUnitType() + "'" +
            ", bedroomsAmount='" + getBedroomsAmount() + "'" +
            ", unitBedrooms='" + getUnitBedrooms() + "'" +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", unitsAmount=" + getUnitsAmount() +
            ", unitsAreaFrom=" + getUnitsAreaFrom() +
            ", unitsAreaTo=" + getUnitsAreaTo() +
            ", unitsAreaFromM2='" + getUnitsAreaFromM2() + "'" +
            ", unitsAreaToM2='" + getUnitsAreaToM2() + "'" +
            ", unitsPriceFrom=" + getUnitsPriceFrom() +
            ", unitsPriceTo=" + getUnitsPriceTo() +
            ", priceCurrency='" + getPriceCurrency() + "'" +
            ", typicalImageUrl='" + getTypicalImageUrl() + "'" +
            ", property=" + getProperty() +
            "}";
    }
}
