package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.Property;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Property entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyRepository extends MongoRepository<Property, Long> {
    Optional<Property> findOneByExtId(Long extId);
}
