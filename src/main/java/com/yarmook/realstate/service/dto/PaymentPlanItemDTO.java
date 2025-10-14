package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.PaymentPlanItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentPlanItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer orderNo;

    @NotNull
    private String paymentTime;

    @NotNull
    private String percentOfPayment;

    private PaymentPlanDTO plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPercentOfPayment() {
        return percentOfPayment;
    }

    public void setPercentOfPayment(String percentOfPayment) {
        this.percentOfPayment = percentOfPayment;
    }

    public PaymentPlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PaymentPlanDTO plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentPlanItemDTO)) {
            return false;
        }

        PaymentPlanItemDTO paymentPlanItemDTO = (PaymentPlanItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentPlanItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentPlanItemDTO{" +
            "id=" + getId() +
            ", orderNo=" + getOrderNo() +
            ", paymentTime='" + getPaymentTime() + "'" +
            ", percentOfPayment='" + getPercentOfPayment() + "'" +
            ", plan=" + getPlan() +
            "}";
    }
}
