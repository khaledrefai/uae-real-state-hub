package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A MapPoint.
 */
@Document(collection = "map_point")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MapPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "map_point_seq";

    @Id
    private Long id;

    @NotNull
    private String name;

    private Double distanceKm;

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MapPoint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MapPoint name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistanceKm() {
        return this.distanceKm;
    }

    public MapPoint distanceKm(Double distanceKm) {
        this.setDistanceKm(distanceKm);
        return this;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public MapPoint property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapPoint)) {
            return false;
        }
        return getId() != null && getId().equals(((MapPoint) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapPoint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", distanceKm=" + getDistanceKm() +
            "}";
    }
}
