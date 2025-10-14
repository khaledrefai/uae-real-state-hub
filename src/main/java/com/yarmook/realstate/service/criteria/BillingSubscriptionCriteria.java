package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.BillingSubscription} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.BillingSubscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /billing-subscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillingSubscriptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter stripeCustomerId;

    private StringFilter stripeSubscriptionId;

    private BooleanFilter cancelAtPeriodEnd;

    private BooleanFilter isActive;

    private LongFilter siteId;

    private Boolean distinct;

    public BillingSubscriptionCriteria() {}

    public BillingSubscriptionCriteria(BillingSubscriptionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.stripeCustomerId = other.optionalStripeCustomerId().map(StringFilter::copy).orElse(null);
        this.stripeSubscriptionId = other.optionalStripeSubscriptionId().map(StringFilter::copy).orElse(null);
        this.cancelAtPeriodEnd = other.optionalCancelAtPeriodEnd().map(BooleanFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.siteId = other.optionalSiteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BillingSubscriptionCriteria copy() {
        return new BillingSubscriptionCriteria(this);
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

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getStripeCustomerId() {
        return stripeCustomerId;
    }

    public Optional<StringFilter> optionalStripeCustomerId() {
        return Optional.ofNullable(stripeCustomerId);
    }

    public StringFilter stripeCustomerId() {
        if (stripeCustomerId == null) {
            setStripeCustomerId(new StringFilter());
        }
        return stripeCustomerId;
    }

    public void setStripeCustomerId(StringFilter stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public StringFilter getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public Optional<StringFilter> optionalStripeSubscriptionId() {
        return Optional.ofNullable(stripeSubscriptionId);
    }

    public StringFilter stripeSubscriptionId() {
        if (stripeSubscriptionId == null) {
            setStripeSubscriptionId(new StringFilter());
        }
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(StringFilter stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public BooleanFilter getCancelAtPeriodEnd() {
        return cancelAtPeriodEnd;
    }

    public Optional<BooleanFilter> optionalCancelAtPeriodEnd() {
        return Optional.ofNullable(cancelAtPeriodEnd);
    }

    public BooleanFilter cancelAtPeriodEnd() {
        if (cancelAtPeriodEnd == null) {
            setCancelAtPeriodEnd(new BooleanFilter());
        }
        return cancelAtPeriodEnd;
    }

    public void setCancelAtPeriodEnd(BooleanFilter cancelAtPeriodEnd) {
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
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
        final BillingSubscriptionCriteria that = (BillingSubscriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(stripeCustomerId, that.stripeCustomerId) &&
            Objects.equals(stripeSubscriptionId, that.stripeSubscriptionId) &&
            Objects.equals(cancelAtPeriodEnd, that.cancelAtPeriodEnd) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(siteId, that.siteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            status,
            startDate,
            endDate,
            stripeCustomerId,
            stripeSubscriptionId,
            cancelAtPeriodEnd,
            isActive,
            siteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingSubscriptionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalStripeCustomerId().map(f -> "stripeCustomerId=" + f + ", ").orElse("") +
            optionalStripeSubscriptionId().map(f -> "stripeSubscriptionId=" + f + ", ").orElse("") +
            optionalCancelAtPeriodEnd().map(f -> "cancelAtPeriodEnd=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalSiteId().map(f -> "siteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
