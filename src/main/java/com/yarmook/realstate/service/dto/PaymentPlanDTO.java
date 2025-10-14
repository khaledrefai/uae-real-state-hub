package com.yarmook.realstate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yarmook.realstate.domain.PaymentPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentPlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer monthsAfterHandover;

    private PropertyDTO property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMonthsAfterHandover() {
        return monthsAfterHandover;
    }

    public void setMonthsAfterHandover(Integer monthsAfterHandover) {
        this.monthsAfterHandover = monthsAfterHandover;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentPlanDTO)) {
            return false;
        }

        PaymentPlanDTO paymentPlanDTO = (PaymentPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentPlanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", monthsAfterHandover=" + getMonthsAfterHandover() +
            ", property=" + getProperty() +
            "}";
    }
}
