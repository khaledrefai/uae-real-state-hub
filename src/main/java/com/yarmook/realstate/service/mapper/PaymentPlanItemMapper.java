package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.PaymentPlan;
import com.yarmook.realstate.domain.PaymentPlanItem;
import com.yarmook.realstate.service.dto.PaymentPlanDTO;
import com.yarmook.realstate.service.dto.PaymentPlanItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentPlanItem} and its DTO {@link PaymentPlanItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentPlanItemMapper extends EntityMapper<PaymentPlanItemDTO, PaymentPlanItem> {
    @Mapping(target = "plan", source = "plan", qualifiedByName = "paymentPlanId")
    PaymentPlanItemDTO toDto(PaymentPlanItem s);

    @Named("paymentPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentPlanDTO toDtoPaymentPlanId(PaymentPlan paymentPlan);
}
