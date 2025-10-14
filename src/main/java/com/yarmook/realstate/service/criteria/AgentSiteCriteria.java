package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.AgentSite} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.AgentSiteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agent-sites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentSiteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter slug;

    private StringFilter displayName;

    private StringFilter theme;

    private StringFilter primaryColor;

    private StringFilter secondaryColor;

    private StringFilter logoUrl;

    private StringFilter contactEmail;

    private StringFilter contactPhone;

    private StringFilter contactWhatsapp;

    private StringFilter domain;

    private BooleanFilter isActive;

    private LongFilter ownerId;

    private LongFilter planId;

    private Boolean distinct;

    public AgentSiteCriteria() {}

    public AgentSiteCriteria(AgentSiteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.displayName = other.optionalDisplayName().map(StringFilter::copy).orElse(null);
        this.theme = other.optionalTheme().map(StringFilter::copy).orElse(null);
        this.primaryColor = other.optionalPrimaryColor().map(StringFilter::copy).orElse(null);
        this.secondaryColor = other.optionalSecondaryColor().map(StringFilter::copy).orElse(null);
        this.logoUrl = other.optionalLogoUrl().map(StringFilter::copy).orElse(null);
        this.contactEmail = other.optionalContactEmail().map(StringFilter::copy).orElse(null);
        this.contactPhone = other.optionalContactPhone().map(StringFilter::copy).orElse(null);
        this.contactWhatsapp = other.optionalContactWhatsapp().map(StringFilter::copy).orElse(null);
        this.domain = other.optionalDomain().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.planId = other.optionalPlanId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgentSiteCriteria copy() {
        return new AgentSiteCriteria(this);
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

    public StringFilter getDisplayName() {
        return displayName;
    }

    public Optional<StringFilter> optionalDisplayName() {
        return Optional.ofNullable(displayName);
    }

    public StringFilter displayName() {
        if (displayName == null) {
            setDisplayName(new StringFilter());
        }
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getTheme() {
        return theme;
    }

    public Optional<StringFilter> optionalTheme() {
        return Optional.ofNullable(theme);
    }

    public StringFilter theme() {
        if (theme == null) {
            setTheme(new StringFilter());
        }
        return theme;
    }

    public void setTheme(StringFilter theme) {
        this.theme = theme;
    }

    public StringFilter getPrimaryColor() {
        return primaryColor;
    }

    public Optional<StringFilter> optionalPrimaryColor() {
        return Optional.ofNullable(primaryColor);
    }

    public StringFilter primaryColor() {
        if (primaryColor == null) {
            setPrimaryColor(new StringFilter());
        }
        return primaryColor;
    }

    public void setPrimaryColor(StringFilter primaryColor) {
        this.primaryColor = primaryColor;
    }

    public StringFilter getSecondaryColor() {
        return secondaryColor;
    }

    public Optional<StringFilter> optionalSecondaryColor() {
        return Optional.ofNullable(secondaryColor);
    }

    public StringFilter secondaryColor() {
        if (secondaryColor == null) {
            setSecondaryColor(new StringFilter());
        }
        return secondaryColor;
    }

    public void setSecondaryColor(StringFilter secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public StringFilter getLogoUrl() {
        return logoUrl;
    }

    public Optional<StringFilter> optionalLogoUrl() {
        return Optional.ofNullable(logoUrl);
    }

    public StringFilter logoUrl() {
        if (logoUrl == null) {
            setLogoUrl(new StringFilter());
        }
        return logoUrl;
    }

    public void setLogoUrl(StringFilter logoUrl) {
        this.logoUrl = logoUrl;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public Optional<StringFilter> optionalContactEmail() {
        return Optional.ofNullable(contactEmail);
    }

    public StringFilter contactEmail() {
        if (contactEmail == null) {
            setContactEmail(new StringFilter());
        }
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public StringFilter getContactPhone() {
        return contactPhone;
    }

    public Optional<StringFilter> optionalContactPhone() {
        return Optional.ofNullable(contactPhone);
    }

    public StringFilter contactPhone() {
        if (contactPhone == null) {
            setContactPhone(new StringFilter());
        }
        return contactPhone;
    }

    public void setContactPhone(StringFilter contactPhone) {
        this.contactPhone = contactPhone;
    }

    public StringFilter getContactWhatsapp() {
        return contactWhatsapp;
    }

    public Optional<StringFilter> optionalContactWhatsapp() {
        return Optional.ofNullable(contactWhatsapp);
    }

    public StringFilter contactWhatsapp() {
        if (contactWhatsapp == null) {
            setContactWhatsapp(new StringFilter());
        }
        return contactWhatsapp;
    }

    public void setContactWhatsapp(StringFilter contactWhatsapp) {
        this.contactWhatsapp = contactWhatsapp;
    }

    public StringFilter getDomain() {
        return domain;
    }

    public Optional<StringFilter> optionalDomain() {
        return Optional.ofNullable(domain);
    }

    public StringFilter domain() {
        if (domain == null) {
            setDomain(new StringFilter());
        }
        return domain;
    }

    public void setDomain(StringFilter domain) {
        this.domain = domain;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public Optional<LongFilter> optionalOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            setOwnerId(new LongFilter());
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getPlanId() {
        return planId;
    }

    public Optional<LongFilter> optionalPlanId() {
        return Optional.ofNullable(planId);
    }

    public LongFilter planId() {
        if (planId == null) {
            setPlanId(new LongFilter());
        }
        return planId;
    }

    public void setPlanId(LongFilter planId) {
        this.planId = planId;
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
        final AgentSiteCriteria that = (AgentSiteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(theme, that.theme) &&
            Objects.equals(primaryColor, that.primaryColor) &&
            Objects.equals(secondaryColor, that.secondaryColor) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(contactPhone, that.contactPhone) &&
            Objects.equals(contactWhatsapp, that.contactWhatsapp) &&
            Objects.equals(domain, that.domain) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            slug,
            displayName,
            theme,
            primaryColor,
            secondaryColor,
            logoUrl,
            contactEmail,
            contactPhone,
            contactWhatsapp,
            domain,
            isActive,
            ownerId,
            planId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentSiteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalDisplayName().map(f -> "displayName=" + f + ", ").orElse("") +
            optionalTheme().map(f -> "theme=" + f + ", ").orElse("") +
            optionalPrimaryColor().map(f -> "primaryColor=" + f + ", ").orElse("") +
            optionalSecondaryColor().map(f -> "secondaryColor=" + f + ", ").orElse("") +
            optionalLogoUrl().map(f -> "logoUrl=" + f + ", ").orElse("") +
            optionalContactEmail().map(f -> "contactEmail=" + f + ", ").orElse("") +
            optionalContactPhone().map(f -> "contactPhone=" + f + ", ").orElse("") +
            optionalContactWhatsapp().map(f -> "contactWhatsapp=" + f + ", ").orElse("") +
            optionalDomain().map(f -> "domain=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalPlanId().map(f -> "planId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
