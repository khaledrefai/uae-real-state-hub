package com.yarmook.realstate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * A PaymentPlan.
 */
@Document(collection = "payment_plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    public static final String SEQUENCE_NAME = "payment_plan_seq";

    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer monthsAfterHandover;

    @DocumentReference(lazy = true)
    @JsonIgnoreProperties(value = { "marker" }, allowSetters = true)
    private Property property;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PaymentPlan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMonthsAfterHandover() {
        return this.monthsAfterHandover;
    }

    public PaymentPlan monthsAfterHandover(Integer monthsAfterHandover) {
        this.setMonthsAfterHandover(monthsAfterHandover);
        return this;
    }

    public void setMonthsAfterHandover(Integer monthsAfterHandover) {
        this.monthsAfterHandover = monthsAfterHandover;
    }

    public Property getProperty() {
        return this.property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public PaymentPlan property(Property property) {
        this.setProperty(property);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentPlan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", monthsAfterHandover=" + getMonthsAfterHandover() +
            "}";
    }
}
