package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A PaymentPlanItem.
 */
@Document(collection = "payment_plan_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentPlanItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "payment_plan_item_seq";

    @Id
    private Long id;

    @NotNull
    private Integer orderNo;

    @NotNull
    private String paymentTime;

    @NotNull
    private String percentOfPayment;

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "property" }, allowSetters = true)
    private PaymentPlan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentPlanItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }

    public PaymentPlanItem orderNo(Integer orderNo) {
        this.setOrderNo(orderNo);
        return this;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getPaymentTime() {
        return this.paymentTime;
    }

    public PaymentPlanItem paymentTime(String paymentTime) {
        this.setPaymentTime(paymentTime);
        return this;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPercentOfPayment() {
        return this.percentOfPayment;
    }

    public PaymentPlanItem percentOfPayment(String percentOfPayment) {
        this.setPercentOfPayment(percentOfPayment);
        return this;
    }

    public void setPercentOfPayment(String percentOfPayment) {
        this.percentOfPayment = percentOfPayment;
    }

    public PaymentPlan getPlan() {
        return this.plan;
    }

    public void setPlan(PaymentPlan paymentPlan) {
        this.plan = paymentPlan;
    }

    public PaymentPlanItem plan(PaymentPlan paymentPlan) {
        this.setPlan(paymentPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentPlanItem)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentPlanItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentPlanItem{" +
            "id=" + getId() +
            ", orderNo=" + getOrderNo() +
            ", paymentTime='" + getPaymentTime() + "'" +
            ", percentOfPayment='" + getPercentOfPayment() + "'" +
            "}";
    }
}
