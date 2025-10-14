package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.BillingSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillingSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private String status;

    @NotNull
    private Instant startDate;

    private Instant endDate;

    private String stripeCustomerId;

    private String stripeSubscriptionId;

    private Boolean cancelAtPeriodEnd;

    @NotNull
    private Boolean isActive;

    private AgentSiteDTO site;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public Boolean getCancelAtPeriodEnd() {
        return cancelAtPeriodEnd;
    }

    public void setCancelAtPeriodEnd(Boolean cancelAtPeriodEnd) {
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public AgentSiteDTO getSite() {
        return site;
    }

    public void setSite(AgentSiteDTO site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillingSubscriptionDTO)) {
            return false;
        }

        BillingSubscriptionDTO billingSubscriptionDTO = (BillingSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billingSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingSubscriptionDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", stripeSubscriptionId='" + getStripeSubscriptionId() + "'" +
            ", cancelAtPeriodEnd='" + getCancelAtPeriodEnd() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", site=" + getSite() +
            "}";
    }
}
