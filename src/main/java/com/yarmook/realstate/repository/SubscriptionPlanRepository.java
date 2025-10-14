package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.SubscriptionPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SubscriptionPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, Long> {}
