package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A PropertyMarker.
 */
@Document(collection = "property_marker")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyMarker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "property_marker_seq";

    @Id
    private Long id;

    @NotNull
    @Indexed(unique = true)
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

    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    @DocumentReference(lazy = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PropertyMarker id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExtId() {
        return this.extId;
    }

    public PropertyMarker extId(Long extId) {
        this.setExtId(extId);
        return this;
    }

    public void setExtId(Long extId) {
        this.extId = extId;
    }

    public String getArea() {
        return this.area;
    }

    public PropertyMarker area(String area) {
        this.setArea(area);
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public LocalDate getCompletionDate() {
        return this.completionDate;
    }

    public PropertyMarker completionDate(LocalDate completionDate) {
        this.setCompletionDate(completionDate);
        return this;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public PropertyMarker latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public PropertyMarker longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public PropertyMarker name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return this.developer;
    }

    public PropertyMarker developer(String developer) {
        this.setDeveloper(developer);
        return this;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getStatus() {
        return this.status;
    }

    public PropertyMarker status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaleStatus() {
        return this.saleStatus;
    }

    public PropertyMarker saleStatus(String saleStatus) {
        this.setSaleStatus(saleStatus);
        return this;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public BigDecimal getMinPrice() {
        return this.minPrice;
    }

    public PropertyMarker minPrice(BigDecimal minPrice) {
        this.setMinPrice(minPrice);
        return this;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public PropertyMarker coverUrl(String coverUrl) {
        this.setCoverUrl(coverUrl);
        return this;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverJson() {
        return this.coverJson;
    }

    public PropertyMarker coverJson(String coverJson) {
        this.setCoverJson(coverJson);
        return this;
    }

    public void setCoverJson(String coverJson) {
        this.coverJson = coverJson;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        if (this.property != null) {
            this.property.setMarker(null);
        }
        if (property != null) {
            property.setMarker(this);
        }
        this.property = property;
    }

    public PropertyMarker property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyMarker)) {
            return false;
        }
        return getId() != null && getId().equals(((PropertyMarker) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyMarker{" +
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
