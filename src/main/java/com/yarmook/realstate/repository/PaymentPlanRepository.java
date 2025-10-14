package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.PaymentPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PaymentPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentPlanRepository extends MongoRepository<PaymentPlan, Long> {}
