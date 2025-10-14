package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.PaymentPlan} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.PaymentPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentPlanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter monthsAfterHandover;

    private LongFilter propertyId;

    private Boolean distinct;

    public PaymentPlanCriteria() {}

    public PaymentPlanCriteria(PaymentPlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.monthsAfterHandover = other.optionalMonthsAfterHandover().map(IntegerFilter::copy).orElse(null);
        this.propertyId = other.optionalPropertyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentPlanCriteria copy() {
        return new PaymentPlanCriteria(this);
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

    public IntegerFilter getMonthsAfterHandover() {
        return monthsAfterHandover;
    }

    public Optional<IntegerFilter> optionalMonthsAfterHandover() {
        return Optional.ofNullable(monthsAfterHandover);
    }

    public IntegerFilter monthsAfterHandover() {
        if (monthsAfterHandover == null) {
            setMonthsAfterHandover(new IntegerFilter());
        }
        return monthsAfterHandover;
    }

    public void setMonthsAfterHandover(IntegerFilter monthsAfterHandover) {
        this.monthsAfterHandover = monthsAfterHandover;
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
        final PaymentPlanCriteria that = (PaymentPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(monthsAfterHandover, that.monthsAfterHandover) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, monthsAfterHandover, propertyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentPlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalMonthsAfterHandover().map(f -> "monthsAfterHandover=" + f + ", ").orElse("") +
            optionalPropertyId().map(f -> "propertyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
