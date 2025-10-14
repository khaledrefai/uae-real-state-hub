package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.AgentProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private String externalUserId;

    @NotNull
    private String fullName;

    @NotNull
    private String companyName;

    @NotNull
    private String email;

    private String phone;

    private String whatsapp;

    private String country;

    private String city;

    private String website;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentProfileDTO)) {
            return false;
        }

        AgentProfileDTO agentProfileDTO = (AgentProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agentProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentProfileDTO{" +
            "id=" + getId() +
            ", externalUserId='" + getExternalUserId() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", whatsapp='" + getWhatsapp() + "'" +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", website='" + getWebsite() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
