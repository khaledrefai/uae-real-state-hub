package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.Facility;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Facility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityRepository extends MongoRepository<Facility, Long> {
    List<Facility> findAllByProperty_Id(Long propertyId);
}
