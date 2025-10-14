package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.AgentSite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentSiteDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String slug;

    @NotNull
    private String displayName;

    @NotNull
    private String theme;

    private String primaryColor;

    private String secondaryColor;

    private String logoUrl;

    @NotNull
    private String contactEmail;

    private String contactPhone;

    private String contactWhatsapp;

    private String domain;

    @NotNull
    private Boolean isActive;

    private AgentProfileDTO owner;

    private SubscriptionPlanDTO plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactWhatsapp() {
        return contactWhatsapp;
    }

    public void setContactWhatsapp(String contactWhatsapp) {
        this.contactWhatsapp = contactWhatsapp;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public AgentProfileDTO getOwner() {
        return owner;
    }

    public void setOwner(AgentProfileDTO owner) {
        this.owner = owner;
    }

    public SubscriptionPlanDTO getPlan() {
        return plan;
    }

    public void setPlan(SubscriptionPlanDTO plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentSiteDTO)) {
            return false;
        }

        AgentSiteDTO agentSiteDTO = (AgentSiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agentSiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentSiteDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", theme='" + getTheme() + "'" +
            ", primaryColor='" + getPrimaryColor() + "'" +
            ", secondaryColor='" + getSecondaryColor() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", contactWhatsapp='" + getContactWhatsapp() + "'" +
            ", domain='" + getDomain() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", owner=" + getOwner() +
            ", plan=" + getPlan() +
            "}";
    }
}
