package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.PropertyMarker;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PropertyMarker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyMarkerRepository extends MongoRepository<PropertyMarker, Long> {
    Optional<PropertyMarker> findOneByExtId(Long extId);
}
