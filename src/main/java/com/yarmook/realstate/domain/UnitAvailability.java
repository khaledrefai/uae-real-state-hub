package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A UnitAvailability.
 */
@Document(collection = "unit_availability")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitAvailability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "unit_availability_seq";

    @Id
    private Long id;

    @NotNull
    private String buildingName;

    private Double areaFrom;

    private String areaUnit;

    private String bedroomType;

    private Instant lastUpdated;

    private String priceCurrency;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private Integer unitsAvailable;

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitAvailability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public UnitAvailability buildingName(String buildingName) {
        this.setBuildingName(buildingName);
        return this;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Double getAreaFrom() {
        return this.areaFrom;
    }

    public UnitAvailability areaFrom(Double areaFrom) {
        this.setAreaFrom(areaFrom);
        return this;
    }

    public void setAreaFrom(Double areaFrom) {
        this.areaFrom = areaFrom;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public UnitAvailability areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public String getBedroomType() {
        return this.bedroomType;
    }

    public UnitAvailability bedroomType(String bedroomType) {
        this.setBedroomType(bedroomType);
        return this;
    }

    public void setBedroomType(String bedroomType) {
        this.bedroomType = bedroomType;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public UnitAvailability lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPriceCurrency() {
        return this.priceCurrency;
    }

    public UnitAvailability priceCurrency(String priceCurrency) {
        this.setPriceCurrency(priceCurrency);
        return this;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public BigDecimal getPriceFrom() {
        return this.priceFrom;
    }

    public UnitAvailability priceFrom(BigDecimal priceFrom) {
        this.setPriceFrom(priceFrom);
        return this;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return this.priceTo;
    }

    public UnitAvailability priceTo(BigDecimal priceTo) {
        this.setPriceTo(priceTo);
        return this;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getUnitsAvailable() {
        return this.unitsAvailable;
    }

    public UnitAvailability unitsAvailable(Integer unitsAvailable) {
        this.setUnitsAvailable(unitsAvailable);
        return this;
    }

    public void setUnitsAvailable(Integer unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public UnitAvailability property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitAvailability)) {
            return false;
        }
        return getId() != null && getId().equals(((UnitAvailability) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitAvailability{" +
            "id=" + getId() +
            ", buildingName='" + getBuildingName() + "'" +
            ", areaFrom=" + getAreaFrom() +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", bedroomType='" + getBedroomType() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", priceCurrency='" + getPriceCurrency() + "'" +
            ", priceFrom=" + getPriceFrom() +
            ", priceTo=" + getPriceTo() +
            ", unitsAvailable=" + getUnitsAvailable() +
            "}";
    }
}
