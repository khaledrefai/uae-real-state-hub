package com.yarmook.realstate.service.criteria;

import com.yarmook.realstate.domain.enumeration.ListingType;
import com.yarmook.realstate.domain.enumeration.PropertyStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.Property} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.PropertyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /properties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ListingType
     */
    public static class ListingTypeFilter extends Filter<ListingType> {

        public ListingTypeFilter() {}

        public ListingTypeFilter(ListingTypeFilter filter) {
            super(filter);
        }

        @Override
        public ListingTypeFilter copy() {
            return new ListingTypeFilter(this);
        }
    }

    /**
     * Class for filtering PropertyStatus
     */
    public static class PropertyStatusFilter extends Filter<PropertyStatus> {

        public PropertyStatusFilter() {}

        public PropertyStatusFilter(PropertyStatusFilter filter) {
            super(filter);
        }

        @Override
        public PropertyStatusFilter copy() {
            return new PropertyStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter extId;

    private StringFilter slug;

    private StringFilter name;

    private StringFilter developer;

    private StringFilter area;

    private StringFilter city;

    private StringFilter country;

    private ListingTypeFilter listingType;

    private PropertyStatusFilter status;

    private StringFilter saleStatus;

    private StringFilter readiness;

    private StringFilter serviceCharge;

    private StringFilter furnishing;

    private BooleanFilter hasEscrow;

    private BooleanFilter postHandover;

    private InstantFilter completionDateTime;

    private BigDecimalFilter minPrice;

    private BigDecimalFilter maxPrice;

    private BigDecimalFilter minPriceAed;

    private StringFilter priceCurrency;

    private DoubleFilter minArea;

    private DoubleFilter maxArea;

    private StringFilter areaUnit;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private StringFilter coverUrl;

    private StringFilter videoUrl;

    private StringFilter brochureUrl;

    private StringFilter layoutsPdfUrl;

    private StringFilter website;

    private InstantFilter publishedAt;

    private InstantFilter updatedAt;

    private LongFilter markerId;

    private Boolean distinct;

    public PropertyCriteria() {}

    public PropertyCriteria(PropertyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.extId = other.optionalExtId().map(LongFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.developer = other.optionalDeveloper().map(StringFilter::copy).orElse(null);
        this.area = other.optionalArea().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.country = other.optionalCountry().map(StringFilter::copy).orElse(null);
        this.listingType = other.optionalListingType().map(ListingTypeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(PropertyStatusFilter::copy).orElse(null);
        this.saleStatus = other.optionalSaleStatus().map(StringFilter::copy).orElse(null);
        this.readiness = other.optionalReadiness().map(StringFilter::copy).orElse(null);
        this.serviceCharge = other.optionalServiceCharge().map(StringFilter::copy).orElse(null);
        this.furnishing = other.optionalFurnishing().map(StringFilter::copy).orElse(null);
        this.hasEscrow = other.optionalHasEscrow().map(BooleanFilter::copy).orElse(null);
        this.postHandover = other.optionalPostHandover().map(BooleanFilter::copy).orElse(null);
        this.completionDateTime = other.optionalCompletionDateTime().map(InstantFilter::copy).orElse(null);
        this.minPrice = other.optionalMinPrice().map(BigDecimalFilter::copy).orElse(null);
        this.maxPrice = other.optionalMaxPrice().map(BigDecimalFilter::copy).orElse(null);
        this.minPriceAed = other.optionalMinPriceAed().map(BigDecimalFilter::copy).orElse(null);
        this.priceCurrency = other.optionalPriceCurrency().map(StringFilter::copy).orElse(null);
        this.minArea = other.optionalMinArea().map(DoubleFilter::copy).orElse(null);
        this.maxArea = other.optionalMaxArea().map(DoubleFilter::copy).orElse(null);
        this.areaUnit = other.optionalAreaUnit().map(StringFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(DoubleFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(DoubleFilter::copy).orElse(null);
        this.coverUrl = other.optionalCoverUrl().map(StringFilter::copy).orElse(null);
        this.videoUrl = other.optionalVideoUrl().map(StringFilter::copy).orElse(null);
        this.brochureUrl = other.optionalBrochureUrl().map(StringFilter::copy).orElse(null);
        this.layoutsPdfUrl = other.optionalLayoutsPdfUrl().map(StringFilter::copy).orElse(null);
        this.website = other.optionalWebsite().map(StringFilter::copy).orElse(null);
        this.publishedAt = other.optionalPublishedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.markerId = other.optionalMarkerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PropertyCriteria copy() {
        return new PropertyCriteria(this);
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

    public StringFilter getSlug() {
        return slug;
    }

    public Optional<StringFilter> optionalSlug() {
        return Optional.ofNullable(slug);
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
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

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public Optional<StringFilter> optionalCountry() {
        return Optional.ofNullable(country);
    }

    public StringFilter country() {
        if (country == null) {
            setCountry(new StringFilter());
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public ListingTypeFilter getListingType() {
        return listingType;
    }

    public Optional<ListingTypeFilter> optionalListingType() {
        return Optional.ofNullable(listingType);
    }

    public ListingTypeFilter listingType() {
        if (listingType == null) {
            setListingType(new ListingTypeFilter());
        }
        return listingType;
    }

    public void setListingType(ListingTypeFilter listingType) {
        this.listingType = listingType;
    }

    public PropertyStatusFilter getStatus() {
        return status;
    }

    public Optional<PropertyStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public PropertyStatusFilter status() {
        if (status == null) {
            setStatus(new PropertyStatusFilter());
        }
        return status;
    }

    public void setStatus(PropertyStatusFilter status) {
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

    public StringFilter getReadiness() {
        return readiness;
    }

    public Optional<StringFilter> optionalReadiness() {
        return Optional.ofNullable(readiness);
    }

    public StringFilter readiness() {
        if (readiness == null) {
            setReadiness(new StringFilter());
        }
        return readiness;
    }

    public void setReadiness(StringFilter readiness) {
        this.readiness = readiness;
    }

    public StringFilter getServiceCharge() {
        return serviceCharge;
    }

    public Optional<StringFilter> optionalServiceCharge() {
        return Optional.ofNullable(serviceCharge);
    }

    public StringFilter serviceCharge() {
        if (serviceCharge == null) {
            setServiceCharge(new StringFilter());
        }
        return serviceCharge;
    }

    public void setServiceCharge(StringFilter serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public StringFilter getFurnishing() {
        return furnishing;
    }

    public Optional<StringFilter> optionalFurnishing() {
        return Optional.ofNullable(furnishing);
    }

    public StringFilter furnishing() {
        if (furnishing == null) {
            setFurnishing(new StringFilter());
        }
        return furnishing;
    }

    public void setFurnishing(StringFilter furnishing) {
        this.furnishing = furnishing;
    }

    public BooleanFilter getHasEscrow() {
        return hasEscrow;
    }

    public Optional<BooleanFilter> optionalHasEscrow() {
        return Optional.ofNullable(hasEscrow);
    }

    public BooleanFilter hasEscrow() {
        if (hasEscrow == null) {
            setHasEscrow(new BooleanFilter());
        }
        return hasEscrow;
    }

    public void setHasEscrow(BooleanFilter hasEscrow) {
        this.hasEscrow = hasEscrow;
    }

    public BooleanFilter getPostHandover() {
        return postHandover;
    }

    public Optional<BooleanFilter> optionalPostHandover() {
        return Optional.ofNullable(postHandover);
    }

    public BooleanFilter postHandover() {
        if (postHandover == null) {
            setPostHandover(new BooleanFilter());
        }
        return postHandover;
    }

    public void setPostHandover(BooleanFilter postHandover) {
        this.postHandover = postHandover;
    }

    public InstantFilter getCompletionDateTime() {
        return completionDateTime;
    }

    public Optional<InstantFilter> optionalCompletionDateTime() {
        return Optional.ofNullable(completionDateTime);
    }

    public InstantFilter completionDateTime() {
        if (completionDateTime == null) {
            setCompletionDateTime(new InstantFilter());
        }
        return completionDateTime;
    }

    public void setCompletionDateTime(InstantFilter completionDateTime) {
        this.completionDateTime = completionDateTime;
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

    public BigDecimalFilter getMaxPrice() {
        return maxPrice;
    }

    public Optional<BigDecimalFilter> optionalMaxPrice() {
        return Optional.ofNullable(maxPrice);
    }

    public BigDecimalFilter maxPrice() {
        if (maxPrice == null) {
            setMaxPrice(new BigDecimalFilter());
        }
        return maxPrice;
    }

    public void setMaxPrice(BigDecimalFilter maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimalFilter getMinPriceAed() {
        return minPriceAed;
    }

    public Optional<BigDecimalFilter> optionalMinPriceAed() {
        return Optional.ofNullable(minPriceAed);
    }

    public BigDecimalFilter minPriceAed() {
        if (minPriceAed == null) {
            setMinPriceAed(new BigDecimalFilter());
        }
        return minPriceAed;
    }

    public void setMinPriceAed(BigDecimalFilter minPriceAed) {
        this.minPriceAed = minPriceAed;
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

    public DoubleFilter getMinArea() {
        return minArea;
    }

    public Optional<DoubleFilter> optionalMinArea() {
        return Optional.ofNullable(minArea);
    }

    public DoubleFilter minArea() {
        if (minArea == null) {
            setMinArea(new DoubleFilter());
        }
        return minArea;
    }

    public void setMinArea(DoubleFilter minArea) {
        this.minArea = minArea;
    }

    public DoubleFilter getMaxArea() {
        return maxArea;
    }

    public Optional<DoubleFilter> optionalMaxArea() {
        return Optional.ofNullable(maxArea);
    }

    public DoubleFilter maxArea() {
        if (maxArea == null) {
            setMaxArea(new DoubleFilter());
        }
        return maxArea;
    }

    public void setMaxArea(DoubleFilter maxArea) {
        this.maxArea = maxArea;
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

    public StringFilter getVideoUrl() {
        return videoUrl;
    }

    public Optional<StringFilter> optionalVideoUrl() {
        return Optional.ofNullable(videoUrl);
    }

    public StringFilter videoUrl() {
        if (videoUrl == null) {
            setVideoUrl(new StringFilter());
        }
        return videoUrl;
    }

    public void setVideoUrl(StringFilter videoUrl) {
        this.videoUrl = videoUrl;
    }

    public StringFilter getBrochureUrl() {
        return brochureUrl;
    }

    public Optional<StringFilter> optionalBrochureUrl() {
        return Optional.ofNullable(brochureUrl);
    }

    public StringFilter brochureUrl() {
        if (brochureUrl == null) {
            setBrochureUrl(new StringFilter());
        }
        return brochureUrl;
    }

    public void setBrochureUrl(StringFilter brochureUrl) {
        this.brochureUrl = brochureUrl;
    }

    public StringFilter getLayoutsPdfUrl() {
        return layoutsPdfUrl;
    }

    public Optional<StringFilter> optionalLayoutsPdfUrl() {
        return Optional.ofNullable(layoutsPdfUrl);
    }

    public StringFilter layoutsPdfUrl() {
        if (layoutsPdfUrl == null) {
            setLayoutsPdfUrl(new StringFilter());
        }
        return layoutsPdfUrl;
    }

    public void setLayoutsPdfUrl(StringFilter layoutsPdfUrl) {
        this.layoutsPdfUrl = layoutsPdfUrl;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public Optional<StringFilter> optionalWebsite() {
        return Optional.ofNullable(website);
    }

    public StringFilter website() {
        if (website == null) {
            setWebsite(new StringFilter());
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public InstantFilter getPublishedAt() {
        return publishedAt;
    }

    public Optional<InstantFilter> optionalPublishedAt() {
        return Optional.ofNullable(publishedAt);
    }

    public InstantFilter publishedAt() {
        if (publishedAt == null) {
            setPublishedAt(new InstantFilter());
        }
        return publishedAt;
    }

    public void setPublishedAt(InstantFilter publishedAt) {
        this.publishedAt = publishedAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getMarkerId() {
        return markerId;
    }

    public Optional<LongFilter> optionalMarkerId() {
        return Optional.ofNullable(markerId);
    }

    public LongFilter markerId() {
        if (markerId == null) {
            setMarkerId(new LongFilter());
        }
        return markerId;
    }

    public void setMarkerId(LongFilter markerId) {
        this.markerId = markerId;
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
        final PropertyCriteria that = (PropertyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(extId, that.extId) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(name, that.name) &&
            Objects.equals(developer, that.developer) &&
            Objects.equals(area, that.area) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(listingType, that.listingType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(saleStatus, that.saleStatus) &&
            Objects.equals(readiness, that.readiness) &&
            Objects.equals(serviceCharge, that.serviceCharge) &&
            Objects.equals(furnishing, that.furnishing) &&
            Objects.equals(hasEscrow, that.hasEscrow) &&
            Objects.equals(postHandover, that.postHandover) &&
            Objects.equals(completionDateTime, that.completionDateTime) &&
            Objects.equals(minPrice, that.minPrice) &&
            Objects.equals(maxPrice, that.maxPrice) &&
            Objects.equals(minPriceAed, that.minPriceAed) &&
            Objects.equals(priceCurrency, that.priceCurrency) &&
            Objects.equals(minArea, that.minArea) &&
            Objects.equals(maxArea, that.maxArea) &&
            Objects.equals(areaUnit, that.areaUnit) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(coverUrl, that.coverUrl) &&
            Objects.equals(videoUrl, that.videoUrl) &&
            Objects.equals(brochureUrl, that.brochureUrl) &&
            Objects.equals(layoutsPdfUrl, that.layoutsPdfUrl) &&
            Objects.equals(website, that.website) &&
            Objects.equals(publishedAt, that.publishedAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(markerId, that.markerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            extId,
            slug,
            name,
            developer,
            area,
            city,
            country,
            listingType,
            status,
            saleStatus,
            readiness,
            serviceCharge,
            furnishing,
            hasEscrow,
            postHandover,
            completionDateTime,
            minPrice,
            maxPrice,
            minPriceAed,
            priceCurrency,
            minArea,
            maxArea,
            areaUnit,
            latitude,
            longitude,
            coverUrl,
            videoUrl,
            brochureUrl,
            layoutsPdfUrl,
            website,
            publishedAt,
            updatedAt,
            markerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalExtId().map(f -> "extId=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDeveloper().map(f -> "developer=" + f + ", ").orElse("") +
            optionalArea().map(f -> "area=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalCountry().map(f -> "country=" + f + ", ").orElse("") +
            optionalListingType().map(f -> "listingType=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSaleStatus().map(f -> "saleStatus=" + f + ", ").orElse("") +
            optionalReadiness().map(f -> "readiness=" + f + ", ").orElse("") +
            optionalServiceCharge().map(f -> "serviceCharge=" + f + ", ").orElse("") +
            optionalFurnishing().map(f -> "furnishing=" + f + ", ").orElse("") +
            optionalHasEscrow().map(f -> "hasEscrow=" + f + ", ").orElse("") +
            optionalPostHandover().map(f -> "postHandover=" + f + ", ").orElse("") +
            optionalCompletionDateTime().map(f -> "completionDateTime=" + f + ", ").orElse("") +
            optionalMinPrice().map(f -> "minPrice=" + f + ", ").orElse("") +
            optionalMaxPrice().map(f -> "maxPrice=" + f + ", ").orElse("") +
            optionalMinPriceAed().map(f -> "minPriceAed=" + f + ", ").orElse("") +
            optionalPriceCurrency().map(f -> "priceCurrency=" + f + ", ").orElse("") +
            optionalMinArea().map(f -> "minArea=" + f + ", ").orElse("") +
            optionalMaxArea().map(f -> "maxArea=" + f + ", ").orElse("") +
            optionalAreaUnit().map(f -> "areaUnit=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalCoverUrl().map(f -> "coverUrl=" + f + ", ").orElse("") +
            optionalVideoUrl().map(f -> "videoUrl=" + f + ", ").orElse("") +
            optionalBrochureUrl().map(f -> "brochureUrl=" + f + ", ").orElse("") +
            optionalLayoutsPdfUrl().map(f -> "layoutsPdfUrl=" + f + ", ").orElse("") +
            optionalWebsite().map(f -> "website=" + f + ", ").orElse("") +
            optionalPublishedAt().map(f -> "publishedAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalMarkerId().map(f -> "markerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
