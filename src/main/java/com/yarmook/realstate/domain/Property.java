package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yarmook.realstate.domain.enumeration.ListingType;
import com.yarmook.realstate.domain.enumeration.PropertyStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A Property.
 */
@Document(collection = "property")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "property_seq";

    @Id
    private Long id;

    @NotNull
    @Indexed(unique = true)
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

    @JsonIgnoreProperties(value = { "property" }, allowSetters = true)
    @DocumentReference(lazy = true)
    private PropertyMarker marker;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Property id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExtId() {
        return this.extId;
    }

    public Property extId(Long extId) {
        this.setExtId(extId);
        return this;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public String getSlug() {
        return this.slug;
    }

    public Property slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return this.name;
    }

    public Property name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return this.developer;
    }

    public Property developer(String developer) {
        this.setDeveloper(developer);
        return this;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getArea() {
        return this.area;
    }

    public Property area(String area) {
        this.setArea(area);
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return this.city;
    }

    public Property city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public Property country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ListingType getListingType() {
        return this.listingType;
    }

    public Property listingType(ListingType listingType) {
        this.setListingType(listingType);
        return this;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public PropertyStatus getStatus() {
        return this.status;
    }

    public Property status(PropertyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public String getSaleStatus() {
        return this.saleStatus;
    }

    public Property saleStatus(String saleStatus) {
        this.setSaleStatus(saleStatus);
        return this;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getReadiness() {
        return this.readiness;
    }

    public Property readiness(String readiness) {
        this.setReadiness(readiness);
        return this;
    }

    public void setReadiness(String readiness) {
        this.readiness = readiness;
    }

    public String getServiceCharge() {
        return this.serviceCharge;
    }

    public Property serviceCharge(String serviceCharge) {
        this.setServiceCharge(serviceCharge);
        return this;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getFurnishing() {
        return this.furnishing;
    }

    public Property furnishing(String furnishing) {
        this.setFurnishing(furnishing);
        return this;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public Boolean getHasEscrow() {
        return this.hasEscrow;
    }

    public Property hasEscrow(Boolean hasEscrow) {
        this.setHasEscrow(hasEscrow);
        return this;
    }

    public void setHasEscrow(Boolean hasEscrow) {
        this.hasEscrow = hasEscrow;
    }

    public Boolean getPostHandover() {
        return this.postHandover;
    }

    public Property postHandover(Boolean postHandover) {
        this.setPostHandover(postHandover);
        return this;
    }

    public void setPostHandover(Boolean postHandover) {
        this.postHandover = postHandover;
    }

    public Instant getCompletionDateTime() {
        return this.completionDateTime;
    }

    public Property completionDateTime(Instant completionDateTime) {
        this.setCompletionDateTime(completionDateTime);
        return this;
    }

    public void setCompletionDateTime(Instant completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    public BigDecimal getMinPrice() {
        return this.minPrice;
    }

    public Property minPrice(BigDecimal minPrice) {
        this.setMinPrice(minPrice);
        return this;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return this.maxPrice;
    }

    public Property maxPrice(BigDecimal maxPrice) {
        this.setMaxPrice(maxPrice);
        return this;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPriceAed() {
        return this.minPriceAed;
    }

    public Property minPriceAed(BigDecimal minPriceAed) {
        this.setMinPriceAed(minPriceAed);
        return this;
    }

    public void setMinPriceAed(BigDecimal minPriceAed) {
        this.minPriceAed = minPriceAed;
    }

    public String getPriceCurrency() {
        return this.priceCurrency;
    }

    public Property priceCurrency(String priceCurrency) {
        this.setPriceCurrency(priceCurrency);
        return this;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public Double getMinArea() {
        return this.minArea;
    }

    public Property minArea(Double minArea) {
        this.setMinArea(minArea);
        return this;
    }

    public void setMinArea(Double minArea) {
        this.minArea = minArea;
    }

    public Double getMaxArea() {
        return this.maxArea;
    }

    public Property maxArea(Double maxArea) {
        this.setMaxArea(maxArea);
        return this;
    }

    public void setMaxArea(Double maxArea) {
        this.maxArea = maxArea;
    }

    public String getAreaUnit() {
        return this.areaUnit;
    }

    public Property areaUnit(String areaUnit) {
        this.setAreaUnit(areaUnit);
        return this;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Property latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Property longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public Property coverUrl(String coverUrl) {
        this.setCoverUrl(coverUrl);
        return this;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverJson() {
        return this.coverJson;
    }

    public Property coverJson(String coverJson) {
        this.setCoverJson(coverJson);
        return this;
    }

    public void setCoverJson(String coverJson) {
        this.coverJson = coverJson;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Property videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getBrochureUrl() {
        return this.brochureUrl;
    }

    public Property brochureUrl(String brochureUrl) {
        this.setBrochureUrl(brochureUrl);
        return this;
    }

    public void setBrochureUrl(String brochureUrl) {
        this.brochureUrl = brochureUrl;
    }

    public String getLayoutsPdfUrl() {
        return this.layoutsPdfUrl;
    }

    public Property layoutsPdfUrl(String layoutsPdfUrl) {
        this.setLayoutsPdfUrl(layoutsPdfUrl);
        return this;
    }

    public void setLayoutsPdfUrl(String layoutsPdfUrl) {
        this.layoutsPdfUrl = layoutsPdfUrl;
    }

    public String getWebsite() {
        return this.website;
    }

    public Property website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOverviewMd() {
        return this.overviewMd;
    }

    public Property overviewMd(String overviewMd) {
        this.setOverviewMd(overviewMd);
        return this;
    }

    public void setOverviewMd(String overviewMd) {
        this.overviewMd = overviewMd;
    }

    public String getRaw() {
        return this.raw;
    }

    public Property raw(String raw) {
        this.setRaw(raw);
        return this;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getBuildingsJson() {
        return this.buildingsJson;
    }

    public Property buildingsJson(String buildingsJson) {
        this.setBuildingsJson(buildingsJson);
        return this;
    }

    public void setBuildingsJson(String buildingsJson) {
        this.buildingsJson = buildingsJson;
    }

    public String getDetailJson() {
        return this.detailJson;
    }

    public Property detailJson(String detailJson) {
        this.setDetailJson(detailJson);
        return this;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }

    public Instant getPublishedAt() {
        return this.publishedAt;
    }

    public Property publishedAt(Instant publishedAt) {
        this.setPublishedAt(publishedAt);
        return this;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Property updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PropertyMarker getMarker() {
        return this.marker;
    }

    public void setMarker(PropertyMarker propertyMarker) {
        this.marker = propertyMarker;
    }

    public Property marker(PropertyMarker propertyMarker) {
        this.setMarker(propertyMarker);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Property)) {
            return false;
        }
        return getId() != null && getId().equals(((Property) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Property{" +
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
            "}";
    }
}
