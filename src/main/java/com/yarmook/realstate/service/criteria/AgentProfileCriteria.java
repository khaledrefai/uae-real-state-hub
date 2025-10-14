package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.AgentProfile} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.AgentProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agent-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter externalUserId;

    private StringFilter fullName;

    private StringFilter companyName;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter whatsapp;

    private StringFilter country;

    private StringFilter city;

    private StringFilter website;

    private BooleanFilter active;

    private Boolean distinct;

    public AgentProfileCriteria() {}

    public AgentProfileCriteria(AgentProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.externalUserId = other.optionalExternalUserId().map(StringFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.companyName = other.optionalCompanyName().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.whatsapp = other.optionalWhatsapp().map(StringFilter::copy).orElse(null);
        this.country = other.optionalCountry().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.website = other.optionalWebsite().map(StringFilter::copy).orElse(null);
        this.active = other.optionalActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgentProfileCriteria copy() {
        return new AgentProfileCriteria(this);
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

    public StringFilter getExternalUserId() {
        return externalUserId;
    }

    public Optional<StringFilter> optionalExternalUserId() {
        return Optional.ofNullable(externalUserId);
    }

    public StringFilter externalUserId() {
        if (externalUserId == null) {
            setExternalUserId(new StringFilter());
        }
        return externalUserId;
    }

    public void setExternalUserId(StringFilter externalUserId) {
        this.externalUserId = externalUserId;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public Optional<StringFilter> optionalCompanyName() {
        return Optional.ofNullable(companyName);
    }

    public StringFilter companyName() {
        if (companyName == null) {
            setCompanyName(new StringFilter());
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getWhatsapp() {
        return whatsapp;
    }

    public Optional<StringFilter> optionalWhatsapp() {
        return Optional.ofNullable(whatsapp);
    }

    public StringFilter whatsapp() {
        if (whatsapp == null) {
            setWhatsapp(new StringFilter());
        }
        return whatsapp;
    }

    public void setWhatsapp(StringFilter whatsapp) {
        this.whatsapp = whatsapp;
    }

    public StringFilter getCountry() {
        return country;
    }

    public Optional<StringFilter> optionalCountry() {
        return Optional.ofNullable(country);
    }

    public StringFilter country() {
        if (country == null) {
            setCountry(new StringFilter());
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public Optional<StringFilter> optionalWebsite() {
        return Optional.ofNullable(website);
    }

    public StringFilter website() {
        if (website == null) {
            setWebsite(new StringFilter());
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public Optional<BooleanFilter> optionalActive() {
        return Optional.ofNullable(active);
    }

    public BooleanFilter active() {
        if (active == null) {
            setActive(new BooleanFilter());
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
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
        final AgentProfileCriteria that = (AgentProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(externalUserId, that.externalUserId) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(whatsapp, that.whatsapp) &&
            Objects.equals(country, that.country) &&
            Objects.equals(city, that.city) &&
            Objects.equals(website, that.website) &&
            Objects.equals(active, that.active) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalUserId, fullName, companyName, email, phone, whatsapp, country, city, website, active, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalExternalUserId().map(f -> "externalUserId=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalCompanyName().map(f -> "companyName=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalWhatsapp().map(f -> "whatsapp=" + f + ", ").orElse("") +
            optionalCountry().map(f -> "country=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalWebsite().map(f -> "website=" + f + ", ").orElse("") +
            optionalActive().map(f -> "active=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
