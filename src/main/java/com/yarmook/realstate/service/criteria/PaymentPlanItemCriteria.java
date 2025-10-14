package com.yarmook.realstate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yarmook.realstate.domain.PaymentPlanItem} entity. This class is used
 * in {@link com.yarmook.realstate.web.rest.PaymentPlanItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-plan-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentPlanItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter orderNo;

    private StringFilter paymentTime;

    private StringFilter percentOfPayment;

    private LongFilter planId;

    private Boolean distinct;

    public PaymentPlanItemCriteria() {}

    public PaymentPlanItemCriteria(PaymentPlanItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.orderNo = other.optionalOrderNo().map(IntegerFilter::copy).orElse(null);
        this.paymentTime = other.optionalPaymentTime().map(StringFilter::copy).orElse(null);
        this.percentOfPayment = other.optionalPercentOfPayment().map(StringFilter::copy).orElse(null);
        this.planId = other.optionalPlanId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentPlanItemCriteria copy() {
        return new PaymentPlanItemCriteria(this);
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

    public IntegerFilter getOrderNo() {
        return orderNo;
    }

    public Optional<IntegerFilter> optionalOrderNo() {
        return Optional.ofNullable(orderNo);
    }

    public IntegerFilter orderNo() {
        if (orderNo == null) {
            setOrderNo(new IntegerFilter());
        }
        return orderNo;
    }

    public void setOrderNo(IntegerFilter orderNo) {
        this.orderNo = orderNo;
    }

    public StringFilter getPaymentTime() {
        return paymentTime;
    }

    public Optional<StringFilter> optionalPaymentTime() {
        return Optional.ofNullable(paymentTime);
    }

    public StringFilter paymentTime() {
        if (paymentTime == null) {
            setPaymentTime(new StringFilter());
        }
        return paymentTime;
    }

    public void setPaymentTime(StringFilter paymentTime) {
        this.paymentTime = paymentTime;
    }

    public StringFilter getPercentOfPayment() {
        return percentOfPayment;
    }

    public Optional<StringFilter> optionalPercentOfPayment() {
        return Optional.ofNullable(percentOfPayment);
    }

    public StringFilter percentOfPayment() {
        if (percentOfPayment == null) {
            setPercentOfPayment(new StringFilter());
        }
        return percentOfPayment;
    }

    public void setPercentOfPayment(StringFilter percentOfPayment) {
        this.percentOfPayment = percentOfPayment;
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
        final PaymentPlanItemCriteria that = (PaymentPlanItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderNo, that.orderNo) &&
            Objects.equals(paymentTime, that.paymentTime) &&
            Objects.equals(percentOfPayment, that.percentOfPayment) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNo, paymentTime, percentOfPayment, planId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentPlanItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrderNo().map(f -> "orderNo=" + f + ", ").orElse("") +
            optionalPaymentTime().map(f -> "paymentTime=" + f + ", ").orElse("") +
            optionalPercentOfPayment().map(f -> "percentOfPayment=" + f + ", ").orElse("") +
            optionalPlanId().map(f -> "planId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
