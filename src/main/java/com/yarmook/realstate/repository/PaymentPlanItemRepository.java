package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.PaymentPlanItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PaymentPlanItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentPlanItemRepository extends MongoRepository<PaymentPlanItem, Long> {}
