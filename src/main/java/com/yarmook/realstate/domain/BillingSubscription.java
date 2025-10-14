package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A BillingSubscription.
 */
@Document(collection = "billing_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillingSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "billing_subscription_seq";

    @Id
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

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "owner", "plan" }, allowSetters = true)
    private AgentSite site;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillingSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public BillingSubscription status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public BillingSubscription startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public BillingSubscription endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getStripeCustomerId() {
        return this.stripeCustomerId;
    }

    public BillingSubscription stripeCustomerId(String stripeCustomerId) {
        this.setStripeCustomerId(stripeCustomerId);
        return this;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return this.stripeSubscriptionId;
    }

    public BillingSubscription stripeSubscriptionId(String stripeSubscriptionId) {
        this.setStripeSubscriptionId(stripeSubscriptionId);
        return this;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public Boolean getCancelAtPeriodEnd() {
        return this.cancelAtPeriodEnd;
    }

    public BillingSubscription cancelAtPeriodEnd(Boolean cancelAtPeriodEnd) {
        this.setCancelAtPeriodEnd(cancelAtPeriodEnd);
        return this;
    }

    public void setCancelAtPeriodEnd(Boolean cancelAtPeriodEnd) {
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BillingSubscription isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public AgentSite getSite() {
        return this.site;
    }

    public void setSite(AgentSite agentSite) {
        this.site = agentSite;
    }

    public BillingSubscription site(AgentSite agentSite) {
        this.setSite(agentSite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillingSubscription)) {
            return false;
        }
        return getId() != null && getId().equals(((BillingSubscription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingSubscription{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", stripeSubscriptionId='" + getStripeSubscriptionId() + "'" +
            ", cancelAtPeriodEnd='" + getCancelAtPeriodEnd() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
