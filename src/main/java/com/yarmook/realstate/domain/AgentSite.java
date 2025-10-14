package com.yarmook.realstate.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A AgentSite.
 */
@Document(collection = "agent_site")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentSite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "agent_site_seq";

    @Id
    private Long id;

    @NotNull
    @Size(min = 3)
    @Indexed(unique = true)
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

    @DocumentReference(lazy = true)
    private AgentProfile owner;

    @DocumentReference(lazy = true)
    private SubscriptionPlan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgentSite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return this.slug;
    }

    public AgentSite slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public AgentSite displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTheme() {
        return this.theme;
    }

    public AgentSite theme(String theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public AgentSite primaryColor(String primaryColor) {
        this.setPrimaryColor(primaryColor);
        return this;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return this.secondaryColor;
    }

    public AgentSite secondaryColor(String secondaryColor) {
        this.setSecondaryColor(secondaryColor);
        return this;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public AgentSite logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public AgentSite contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public AgentSite contactPhone(String contactPhone) {
        this.setContactPhone(contactPhone);
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactWhatsapp() {
        return this.contactWhatsapp;
    }

    public AgentSite contactWhatsapp(String contactWhatsapp) {
        this.setContactWhatsapp(contactWhatsapp);
        return this;
    }

    public void setContactWhatsapp(String contactWhatsapp) {
        this.contactWhatsapp = contactWhatsapp;
    }

    public String getDomain() {
        return this.domain;
    }

    public AgentSite domain(String domain) {
        this.setDomain(domain);
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AgentSite isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public AgentProfile getOwner() {
        return this.owner;
    }

    public void setOwner(AgentProfile agentProfile) {
        this.owner = agentProfile;
    }

    public AgentSite owner(AgentProfile agentProfile) {
        this.setOwner(agentProfile);
        return this;
    }

    public SubscriptionPlan getPlan() {
        return this.plan;
    }

    public void setPlan(SubscriptionPlan subscriptionPlan) {
        this.plan = subscriptionPlan;
    }

    public AgentSite plan(SubscriptionPlan subscriptionPlan) {
        this.setPlan(subscriptionPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentSite)) {
            return false;
        }
        return getId() != null && getId().equals(((AgentSite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentSite{" +
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
            "}";
    }
}
