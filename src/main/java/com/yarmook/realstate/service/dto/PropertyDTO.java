package com.yarmook.realstate.service.dto;

import com.yarmook.realstate.domain.enumeration.ListingType;
import com.yarmook.realstate.domain.enumeration.PropertyStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.Property} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyDTO implements Serializable {

    private Long id;

    @NotNull
    private Long extId;

    private String slug;

    @NotNull
    private String name;

    private String developer;

    private String area;

    private String city;

    private String country;

    private ListingType listingType;

    private PropertyStatus status;

    private String saleStatus;

    private String readiness;

    private String serviceCharge;

    private String furnishing;

    private Boolean hasEscrow;

    private Boolean postHandover;

    private Instant completionDateTime;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private BigDecimal minPriceAed;

    private String priceCurrency;

    private Double minArea;

    private Double maxArea;

    private String areaUnit;

    private Double latitude;

    private Double longitude;

    private String coverUrl;

    private String coverJson;

    private String videoUrl;

    private String brochureUrl;

    private String layoutsPdfUrl;

    private String website;

    private String overviewMd;

    private String raw;
    private String buildingsJson;

    private String detailJson;

    private Instant publishedAt;

    private Instant updatedAt;

    private PropertyMarkerDTO marker;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getReadiness() {
        return readiness;
    }

    public void setReadiness(String readiness) {
        this.readiness = readiness;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public Boolean getHasEscrow() {
        return hasEscrow;
    }

    public void setHasEscrow(Boolean hasEscrow) {
        this.hasEscrow = hasEscrow;
    }

    public Boolean getPostHandover() {
        return postHandover;
    }

    public void setPostHandover(Boolean postHandover) {
        this.postHandover = postHandover;
    }

    public Instant getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(Instant completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPriceAed() {
        return minPriceAed;
    }

    public void setMinPriceAed(BigDecimal minPriceAed) {
        this.minPriceAed = minPriceAed;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public Double getMinArea() {
        return minArea;
    }

    public void setMinArea(Double minArea) {
        this.minArea = minArea;
    }

    public Double getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(Double maxArea) {
        this.maxArea = maxArea;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getBrochureUrl() {
        return brochureUrl;
    }

    public void setBrochureUrl(String brochureUrl) {
        this.brochureUrl = brochureUrl;
    }

    public String getLayoutsPdfUrl() {
        return layoutsPdfUrl;
    }

    public void setLayoutsPdfUrl(String layoutsPdfUrl) {
        this.layoutsPdfUrl = layoutsPdfUrl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOverviewMd() {
        return overviewMd;
    }

    public void setOverviewMd(String overviewMd) {
        this.overviewMd = overviewMd;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getBuildingsJson() {
        return buildingsJson;
    }

    public void setBuildingsJson(String buildingsJson) {
        this.buildingsJson = buildingsJson;
    }

    public String getDetailJson() {
        return detailJson;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PropertyMarkerDTO getMarker() {
        return marker;
    }

    public void setMarker(PropertyMarkerDTO marker) {
        this.marker = marker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyDTO)) {
            return false;
        }

        PropertyDTO propertyDTO = (PropertyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, propertyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyDTO{" +
            "id=" + getId() +
            ", extId=" + getExtId() +
            ", slug='" + getSlug() + "'" +
            ", name='" + getName() + "'" +
            ", developer='" + getDeveloper() + "'" +
            ", area='" + getArea() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", listingType='" + getListingType() + "'" +
            ", status='" + getStatus() + "'" +
            ", saleStatus='" + getSaleStatus() + "'" +
            ", readiness='" + getReadiness() + "'" +
            ", serviceCharge='" + getServiceCharge() + "'" +
            ", furnishing='" + getFurnishing() + "'" +
            ", hasEscrow='" + getHasEscrow() + "'" +
            ", postHandover='" + getPostHandover() + "'" +
            ", completionDateTime='" + getCompletionDateTime() + "'" +
            ", minPrice=" + getMinPrice() +
            ", maxPrice=" + getMaxPrice() +
            ", minPriceAed=" + getMinPriceAed() +
            ", priceCurrency='" + getPriceCurrency() + "'" +
            ", minArea=" + getMinArea() +
            ", maxArea=" + getMaxArea() +
            ", areaUnit='" + getAreaUnit() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", coverUrl='" + getCoverUrl() + "'" +
            ", coverJson='" + getCoverJson() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", brochureUrl='" + getBrochureUrl() + "'" +
            ", layoutsPdfUrl='" + getLayoutsPdfUrl() + "'" +
            ", website='" + getWebsite() + "'" +
            ", overviewMd='" + getOverviewMd() + "'" +
            ", raw='" + getRaw() + "'" +
            ", buildingsJson='" + getBuildingsJson() + "'" +
            ", detailJson='" + getDetailJson() + "'" +
            ", publishedAt='" + getPublishedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", marker=" + getMarker() +
            "}";
    }
}
