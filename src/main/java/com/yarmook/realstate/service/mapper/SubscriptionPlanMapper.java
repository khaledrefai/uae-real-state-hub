package com.yarmook.realstate.service.mapper;

import com.yarmook.realstate.domain.SubscriptionPlan;
import com.yarmook.realstate.service.dto.SubscriptionPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionPlan} and its DTO {@link SubscriptionPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionPlanMapper extends EntityMapper<SubscriptionPlanDTO, SubscriptionPlan> {}
