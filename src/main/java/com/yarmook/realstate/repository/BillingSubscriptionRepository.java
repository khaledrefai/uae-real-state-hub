package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.BillingSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the BillingSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillingSubscriptionRepository extends MongoRepository<BillingSubscription, Long> {}
