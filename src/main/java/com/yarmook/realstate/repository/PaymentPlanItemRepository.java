package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.PaymentPlanItem;
import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PaymentPlanItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentPlanItemRepository extends MongoRepository<PaymentPlanItem, Long> {
    List<PaymentPlanItem> findAllByPlan_IdIn(Collection<Long> planIds);
}
