package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.PropertyMarker} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyMarkerDTO implements Serializable {

    private Long id;

    @NotNull
    private Long extId;

    private String area;

    private LocalDate completionDate;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private String name;

    private String developer;

    private String status;

    private String saleStatus;

    private BigDecimal minPrice;

    private String coverUrl;

    private String coverJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExtId() {
        return extId;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverJson() {
        return coverJson;
    }

    public void setCoverJson(String coverJson) {
        this.coverJson = coverJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyMarkerDTO)) {
            return false;
        }

        PropertyMarkerDTO propertyMarkerDTO = (PropertyMarkerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, propertyMarkerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyMarkerDTO{" +
            "id=" + getId() +
            ", extId=" + getExtId() +
            ", area='" + getArea() + "'" +
            ", completionDate='" + getCompletionDate() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", name='" + getName() + "'" +
            ", developer='" + getDeveloper() + "'" +
            ", status='" + getStatus() + "'" +
            ", saleStatus='" + getSaleStatus() + "'" +
            ", minPrice=" + getMinPrice() +
            ", coverUrl='" + getCoverUrl() + "'" +
            ", coverJson='" + getCoverJson() + "'" +
            "}";
    }
}
