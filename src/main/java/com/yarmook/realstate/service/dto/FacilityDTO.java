package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.Facility} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacilityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String source;

    private String imageUrl;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        if (!(o instanceof FacilityDTO)) {
            return false;
        }

        FacilityDTO facilityDTO = (FacilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacilityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", source='" + getSource() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", property=" + getProperty() +
            "}";
    }
}
