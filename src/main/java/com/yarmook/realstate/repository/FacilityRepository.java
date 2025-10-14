package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Facility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityRepository extends MongoRepository<Facility, Long> {}
