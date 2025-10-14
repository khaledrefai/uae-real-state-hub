package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.UnitAvailability} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitAvailabilityDTO implements Serializable {

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

    private PropertyDTO property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Double getAreaFrom() {
        return areaFrom;
    }

    public void setAreaFrom(Double areaFrom) {
        this.areaFrom = areaFrom;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public String getBedroomType() {
        return bedroomType;
    }

    public void setBedroomType(String bedroomType) {
        this.bedroomType = bedroomType;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getUnitsAvailable() {
        return unitsAvailable;
    }

    public void setUnitsAvailable(Integer unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
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
        if (!(o instanceof UnitAvailabilityDTO)) {
            return false;
        }

        UnitAvailabilityDTO unitAvailabilityDTO = (UnitAvailabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitAvailabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitAvailabilityDTO{" +
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
            ", property=" + getProperty() +
            "}";
    }
}
