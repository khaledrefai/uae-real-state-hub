package com.yarmook.realstate.service.dto;

import com.yarmook.realstate.domain.enumeration.LeadStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.ContactLead} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactLeadDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String email;

    private String phone;

    private String whatsapp;

    private String message;

    private String source;

    private String utm;

    private Instant createdAt;

    private LeadStatus status;

    private AgentSiteDTO site;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUtm() {
        return utm;
    }

    public void setUtm(String utm) {
        this.utm = utm;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public AgentSiteDTO getSite() {
        return site;
    }

    public void setSite(AgentSiteDTO site) {
        this.site = site;
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
        if (!(o instanceof ContactLeadDTO)) {
            return false;
        }

        ContactLeadDTO contactLeadDTO = (ContactLeadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactLeadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactLeadDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", whatsapp='" + getWhatsapp() + "'" +
            ", message='" + getMessage() + "'" +
            ", source='" + getSource() + "'" +
            ", utm='" + getUtm() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", site=" + getSite() +
            ", property=" + getProperty() +
            "}";
    }
}
