package com.yarmook.realstate.service.criteria;

import com.yarmook.realstate.domain.enumeration.Currency;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.SubscriptionPlan} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.SubscriptionPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscription-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionPlanCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Currency
     */
    public static class CurrencyFilter extends Filter<Currency> {

        public CurrencyFilter() {}

        public CurrencyFilter(CurrencyFilter filter) {
            super(filter);
        }

        @Override
        public CurrencyFilter copy() {
            return new CurrencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private BigDecimalFilter priceMonthly;

    private BigDecimalFilter priceYearly;

    private CurrencyFilter currency;

    private StringFilter stripePriceMonthlyId;

    private StringFilter stripePriceYearlyId;

    private IntegerFilter maxListings;

    private Boolean distinct;

    public SubscriptionPlanCriteria() {}

    public SubscriptionPlanCriteria(SubscriptionPlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.priceMonthly = other.optionalPriceMonthly().map(BigDecimalFilter::copy).orElse(null);
        this.priceYearly = other.optionalPriceYearly().map(BigDecimalFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(CurrencyFilter::copy).orElse(null);
        this.stripePriceMonthlyId = other.optionalStripePriceMonthlyId().map(StringFilter::copy).orElse(null);
        this.stripePriceYearlyId = other.optionalStripePriceYearlyId().map(StringFilter::copy).orElse(null);
        this.maxListings = other.optionalMaxListings().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SubscriptionPlanCriteria copy() {
        return new SubscriptionPlanCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public BigDecimalFilter getPriceMonthly() {
        return priceMonthly;
    }

    public Optional<BigDecimalFilter> optionalPriceMonthly() {
        return Optional.ofNullable(priceMonthly);
    }

    public BigDecimalFilter priceMonthly() {
        if (priceMonthly == null) {
            setPriceMonthly(new BigDecimalFilter());
        }
        return priceMonthly;
    }

    public void setPriceMonthly(BigDecimalFilter priceMonthly) {
        this.priceMonthly = priceMonthly;
    }

    public BigDecimalFilter getPriceYearly() {
        return priceYearly;
    }

    public Optional<BigDecimalFilter> optionalPriceYearly() {
        return Optional.ofNullable(priceYearly);
    }

    public BigDecimalFilter priceYearly() {
        if (priceYearly == null) {
            setPriceYearly(new BigDecimalFilter());
        }
        return priceYearly;
    }

    public void setPriceYearly(BigDecimalFilter priceYearly) {
        this.priceYearly = priceYearly;
    }

    public CurrencyFilter getCurrency() {
        return currency;
    }

    public Optional<CurrencyFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public CurrencyFilter currency() {
        if (currency == null) {
            setCurrency(new CurrencyFilter());
        }
        return currency;
    }

    public void setCurrency(CurrencyFilter currency) {
        this.currency = currency;
    }

    public StringFilter getStripePriceMonthlyId() {
        return stripePriceMonthlyId;
    }

    public Optional<StringFilter> optionalStripePriceMonthlyId() {
        return Optional.ofNullable(stripePriceMonthlyId);
    }

    public StringFilter stripePriceMonthlyId() {
        if (stripePriceMonthlyId == null) {
            setStripePriceMonthlyId(new StringFilter());
        }
        return stripePriceMonthlyId;
    }

    public void setStripePriceMonthlyId(StringFilter stripePriceMonthlyId) {
        this.stripePriceMonthlyId = stripePriceMonthlyId;
    }

    public StringFilter getStripePriceYearlyId() {
        return stripePriceYearlyId;
    }

    public Optional<StringFilter> optionalStripePriceYearlyId() {
        return Optional.ofNullable(stripePriceYearlyId);
    }

    public StringFilter stripePriceYearlyId() {
        if (stripePriceYearlyId == null) {
            setStripePriceYearlyId(new StringFilter());
        }
        return stripePriceYearlyId;
    }

    public void setStripePriceYearlyId(StringFilter stripePriceYearlyId) {
        this.stripePriceYearlyId = stripePriceYearlyId;
    }

    public IntegerFilter getMaxListings() {
        return maxListings;
    }

    public Optional<IntegerFilter> optionalMaxListings() {
        return Optional.ofNullable(maxListings);
    }

    public IntegerFilter maxListings() {
        if (maxListings == null) {
            setMaxListings(new IntegerFilter());
        }
        return maxListings;
    }

    public void setMaxListings(IntegerFilter maxListings) {
        this.maxListings = maxListings;
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
        final SubscriptionPlanCriteria that = (SubscriptionPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(priceMonthly, that.priceMonthly) &&
            Objects.equals(priceYearly, that.priceYearly) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(stripePriceMonthlyId, that.stripePriceMonthlyId) &&
            Objects.equals(stripePriceYearlyId, that.stripePriceYearlyId) &&
            Objects.equals(maxListings, that.maxListings) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            priceMonthly,
            priceYearly,
            currency,
            stripePriceMonthlyId,
            stripePriceYearlyId,
            maxListings,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionPlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPriceMonthly().map(f -> "priceMonthly=" + f + ", ").orElse("") +
            optionalPriceYearly().map(f -> "priceYearly=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStripePriceMonthlyId().map(f -> "stripePriceMonthlyId=" + f + ", ").orElse("") +
            optionalStripePriceYearlyId().map(f -> "stripePriceYearlyId=" + f + ", ").orElse("") +
            optionalMaxListings().map(f -> "maxListings=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
