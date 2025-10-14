package com.yarmook.realstate.domain;

import com.yarmook.realstate.domain.enumeration.Currency;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A SubscriptionPlan.
 */
@Document(collection = "subscription_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "subscription_plan_seq";

    @Id
    private Long id;

    @NotNull
    @Indexed(unique = true)
    private String code;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal priceMonthly;

    private BigDecimal priceYearly;

    @NotNull
    private Currency currency;

    private String stripePriceMonthlyId;

    private String stripePriceYearlyId;

    @NotNull
    private Integer maxListings;

    private String features;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public SubscriptionPlan code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public SubscriptionPlan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceMonthly() {
        return this.priceMonthly;
    }

    public SubscriptionPlan priceMonthly(BigDecimal priceMonthly) {
        this.setPriceMonthly(priceMonthly);
        return this;
    }

    public void setPriceMonthly(BigDecimal priceMonthly) {
        this.priceMonthly = priceMonthly;
    }

    public BigDecimal getPriceYearly() {
        return this.priceYearly;
    }

    public SubscriptionPlan priceYearly(BigDecimal priceYearly) {
        this.setPriceYearly(priceYearly);
        return this;
    }

    public void setPriceYearly(BigDecimal priceYearly) {
        this.priceYearly = priceYearly;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public SubscriptionPlan currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getStripePriceMonthlyId() {
        return this.stripePriceMonthlyId;
    }

    public SubscriptionPlan stripePriceMonthlyId(String stripePriceMonthlyId) {
        this.setStripePriceMonthlyId(stripePriceMonthlyId);
        return this;
    }

    public void setStripePriceMonthlyId(String stripePriceMonthlyId) {
        this.stripePriceMonthlyId = stripePriceMonthlyId;
    }

    public String getStripePriceYearlyId() {
        return this.stripePriceYearlyId;
    }

    public SubscriptionPlan stripePriceYearlyId(String stripePriceYearlyId) {
        this.setStripePriceYearlyId(stripePriceYearlyId);
        return this;
    }

    public void setStripePriceYearlyId(String stripePriceYearlyId) {
        this.stripePriceYearlyId = stripePriceYearlyId;
    }

    public Integer getMaxListings() {
        return this.maxListings;
    }

    public SubscriptionPlan maxListings(Integer maxListings) {
        this.setMaxListings(maxListings);
        return this;
    }

    public void setMaxListings(Integer maxListings) {
        this.maxListings = maxListings;
    }

    public String getFeatures() {
        return this.features;
    }

    public SubscriptionPlan features(String features) {
        this.setFeatures(features);
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionPlan{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", priceMonthly=" + getPriceMonthly() +
            ", priceYearly=" + getPriceYearly() +
            ", currency='" + getCurrency() + "'" +
            ", stripePriceMonthlyId='" + getStripePriceMonthlyId() + "'" +
            ", stripePriceYearlyId='" + getStripePriceYearlyId() + "'" +
            ", maxListings=" + getMaxListings() +
            ", features='" + getFeatures() + "'" +
            "}";
    }
}
