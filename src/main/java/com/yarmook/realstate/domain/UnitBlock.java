package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A UnitBlock.
 */
@Document(collection = "unit_block")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "unit_block_seq";

    @Id
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

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitBlock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalizedType() {
        return this.normalizedType;
    }

    public UnitBlock normalizedType(String normalizedType) {
        this.setNormalizedType(normalizedType);
        return this;
    }

    public void setNormalizedType(String normalizedType) {
        this.normalizedType = normalizedType;
    }

    public String getUnitType() {
        return this.unitType;
    }

    public UnitBlock unitType(String unitType) {
        this.setUnitType(unitType);
        return this;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getBedroomsAmount() {
        return this.bedroomsAmount;
    }

    public UnitBlock bedroomsAmount(String bedroomsAmount) {
        this.setBedroomsAmount(bedroomsAmount);
        return this;
    }

    public void setBedroomsAmount(String bedroomsAmount) {
        this.bedroomsAmount = bedroomsAmount;
    }

    public String getUnitBedrooms() {
        return this.unitBedrooms;
    }

    public UnitBlock unitBedrooms(String unitBedrooms) {
        this.setUnitBedrooms(unitBedrooms);
        return this;
    }

    public void setUnitBedrooms(String unitBedrooms) {
        this.unitBedrooms = unitBedrooms;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public UnitBlock areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public Integer getUnitsAmount() {
        return this.unitsAmount;
    }

    public UnitBlock unitsAmount(Integer unitsAmount) {
        this.setUnitsAmount(unitsAmount);
        return this;
    }

    public void setUnitsAmount(Integer unitsAmount) {
        this.unitsAmount = unitsAmount;
    }

    public Double getUnitsAreaFrom() {
        return this.unitsAreaFrom;
    }

    public UnitBlock unitsAreaFrom(Double unitsAreaFrom) {
        this.setUnitsAreaFrom(unitsAreaFrom);
        return this;
    }

    public void setUnitsAreaFrom(Double unitsAreaFrom) {
        this.unitsAreaFrom = unitsAreaFrom;
    }

    public Double getUnitsAreaTo() {
        return this.unitsAreaTo;
    }

    public UnitBlock unitsAreaTo(Double unitsAreaTo) {
        this.setUnitsAreaTo(unitsAreaTo);
        return this;
    }

    public void setUnitsAreaTo(Double unitsAreaTo) {
        this.unitsAreaTo = unitsAreaTo;
    }

    public String getUnitsAreaFromM2() {
        return this.unitsAreaFromM2;
    }

    public UnitBlock unitsAreaFromM2(String unitsAreaFromM2) {
        this.setUnitsAreaFromM2(unitsAreaFromM2);
        return this;
    }

    public void setUnitsAreaFromM2(String unitsAreaFromM2) {
        this.unitsAreaFromM2 = unitsAreaFromM2;
    }

    public String getUnitsAreaToM2() {
        return this.unitsAreaToM2;
    }

    public UnitBlock unitsAreaToM2(String unitsAreaToM2) {
        this.setUnitsAreaToM2(unitsAreaToM2);
        return this;
    }

    public void setUnitsAreaToM2(String unitsAreaToM2) {
        this.unitsAreaToM2 = unitsAreaToM2;
    }

    public BigDecimal getUnitsPriceFrom() {
        return this.unitsPriceFrom;
    }

    public UnitBlock unitsPriceFrom(BigDecimal unitsPriceFrom) {
        this.setUnitsPriceFrom(unitsPriceFrom);
        return this;
    }

    public void setUnitsPriceFrom(BigDecimal unitsPriceFrom) {
        this.unitsPriceFrom = unitsPriceFrom;
    }

    public BigDecimal getUnitsPriceTo() {
        return this.unitsPriceTo;
    }

    public UnitBlock unitsPriceTo(BigDecimal unitsPriceTo) {
        this.setUnitsPriceTo(unitsPriceTo);
        return this;
    }

    public void setUnitsPriceTo(BigDecimal unitsPriceTo) {
        this.unitsPriceTo = unitsPriceTo;
    }

    public String getPriceCurrency() {
        return this.priceCurrency;
    }

    public UnitBlock priceCurrency(String priceCurrency) {
        this.setPriceCurrency(priceCurrency);
        return this;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getTypicalImageUrl() {
        return this.typicalImageUrl;
    }

    public UnitBlock typicalImageUrl(String typicalImageUrl) {
        this.setTypicalImageUrl(typicalImageUrl);
        return this;
    }

    public void setTypicalImageUrl(String typicalImageUrl) {
        this.typicalImageUrl = typicalImageUrl;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public UnitBlock property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitBlock)) {
            return false;
        }
        return getId() != null && getId().equals(((UnitBlock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitBlock{" +
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
            "}";
    }
}
