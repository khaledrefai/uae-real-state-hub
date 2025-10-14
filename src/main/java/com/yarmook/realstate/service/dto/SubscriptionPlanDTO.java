package com.yarmook.realstate.service.dto;

import com.yarmook.realstate.domain.enumeration.Currency;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.SubscriptionPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionPlanDTO implements Serializable {

    private Long id;

    @NotNull
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceMonthly() {
        return priceMonthly;
    }

    public void setPriceMonthly(BigDecimal priceMonthly) {
        this.priceMonthly = priceMonthly;
    }

    public BigDecimal getPriceYearly() {
        return priceYearly;
    }

    public void setPriceYearly(BigDecimal priceYearly) {
        this.priceYearly = priceYearly;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getStripePriceMonthlyId() {
        return stripePriceMonthlyId;
    }

    public void setStripePriceMonthlyId(String stripePriceMonthlyId) {
        this.stripePriceMonthlyId = stripePriceMonthlyId;
    }

    public String getStripePriceYearlyId() {
        return stripePriceYearlyId;
    }

    public void setStripePriceYearlyId(String stripePriceYearlyId) {
        this.stripePriceYearlyId = stripePriceYearlyId;
    }

    public Integer getMaxListings() {
        return maxListings;
    }

    public void setMaxListings(Integer maxListings) {
        this.maxListings = maxListings;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionPlanDTO)) {
            return false;
        }

        SubscriptionPlanDTO subscriptionPlanDTO = (SubscriptionPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionPlanDTO{" +
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
