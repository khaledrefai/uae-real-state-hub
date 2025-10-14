package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.PaymentPlan;
import com.yarmook.realstate.domain.Property;
import com.yarmook.realstate.service.dto.PaymentPlanDTO;
import com.yarmook.realstate.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentPlan} and its DTO {@link PaymentPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentPlanMapper extends EntityMapper<PaymentPlanDTO, PaymentPlan> {
    @Mapping(target = "property", source = "property", qualifiedByName = "propertyId")
    PaymentPlanDTO toDto(PaymentPlan s);

    @Named("propertyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PropertyDTO toDtoPropertyId(Property property);
}
