package com.yarmook.realstate.service.criteria;

import com.yarmook.realstate.domain.enumeration.LeadStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.ContactLead} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.ContactLeadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contact-leads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactLeadCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LeadStatus
     */
    public static class LeadStatusFilter extends Filter<LeadStatus> {

        public LeadStatusFilter() {}

        public LeadStatusFilter(LeadStatusFilter filter) {
            super(filter);
        }

        @Override
        public LeadStatusFilter copy() {
            return new LeadStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter whatsapp;

    private StringFilter message;

    private StringFilter source;

    private InstantFilter createdAt;

    private LeadStatusFilter status;

    private LongFilter siteId;

    private LongFilter propertyId;

    private Boolean distinct;

    public ContactLeadCriteria() {}

    public ContactLeadCriteria(ContactLeadCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.whatsapp = other.optionalWhatsapp().map(StringFilter::copy).orElse(null);
        this.message = other.optionalMessage().map(StringFilter::copy).orElse(null);
        this.source = other.optionalSource().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(LeadStatusFilter::copy).orElse(null);
        this.siteId = other.optionalSiteId().map(LongFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContactLeadCriteria copy() {
        return new ContactLeadCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getMessage() {
        return message;
    }

    public Optional<StringFilter> optionalMessage() {
        return Optional.ofNullable(message);
    }

    public StringFilter message() {
        if (message == null) {
            setMessage(new StringFilter());
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getSource() {
        return source;
    }

    public Optional<StringFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public StringFilter source() {
        if (source == null) {
            setSource(new StringFilter());
        }
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LeadStatusFilter getStatus() {
        return status;
    }

    public Optional<LeadStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public LeadStatusFilter status() {
        if (status == null) {
            setStatus(new LeadStatusFilter());
        }
        return status;
    }

    public void setStatus(LeadStatusFilter status) {
        this.status = status;
    }

    public LongFilter getSiteId() {
        return siteId;
    }

    public Optional<LongFilter> optionalSiteId() {
        return Optional.ofNullable(siteId);
    }

    public LongFilter siteId() {
        if (siteId == null) {
            setSiteId(new LongFilter());
        }
        return siteId;
    }

    public void setSiteId(LongFilter siteId) {
        this.siteId = siteId;
    }

    public LongFilter getPropertyId() {
        return propertyId;
    }

    public Optional<LongFilter> optionalPropertyId() {
        return Optional.ofNullable(propertyId);
    }

    public LongFilter propertyId() {
        if (propertyId == null) {
            setPropertyId(new LongFilter());
        }
        return propertyId;
    }

    public void setPropertyId(LongFilter propertyId) {
        this.propertyId = propertyId;
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
        final ContactLeadCriteria that = (ContactLeadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(whatsapp, that.whatsapp) &&
            Objects.equals(message, that.message) &&
            Objects.equals(source, that.source) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(siteId, that.siteId) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phone, whatsapp, message, source, createdAt, status, siteId, propertyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactLeadCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalWhatsapp().map(f -> "whatsapp=" + f + ", ").orElse("") +
            optionalMessage().map(f -> "message=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalSiteId().map(f -> "siteId=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
