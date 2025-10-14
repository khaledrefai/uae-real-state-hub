package com.yarmook.realstate.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A AgentProfile.
 */
@Document(collection = "agent_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "agent_profile_seq";

    @Id
    private Long id;

    @NotNull
    @Indexed(unique = true)
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgentProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return this.externalUserId;
    }

    public AgentProfile externalUserId(String externalUserId) {
        this.setExternalUserId(externalUserId);
        return this;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public AgentProfile fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public AgentProfile companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return this.email;
    }

    public AgentProfile email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public AgentProfile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWhatsapp() {
        return this.whatsapp;
    }

    public AgentProfile whatsapp(String whatsapp) {
        this.setWhatsapp(whatsapp);
        return this;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCountry() {
        return this.country;
    }

    public AgentProfile country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public AgentProfile city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return this.website;
    }

    public AgentProfile website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AgentProfile active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((AgentProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentProfile{" +
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
