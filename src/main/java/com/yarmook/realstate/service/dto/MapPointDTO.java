package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.MapPoint} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MapPointDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double distanceKm;

    private PropertyDTO property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
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
        if (!(o instanceof MapPointDTO)) {
            return false;
        }

        MapPointDTO mapPointDTO = (MapPointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mapPointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MapPointDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", distanceKm=" + getDistanceKm() +
            ", property=" + getProperty() +
            "}";
    }
}
