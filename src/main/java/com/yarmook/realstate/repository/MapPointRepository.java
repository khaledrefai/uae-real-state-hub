package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.MapPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the MapPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MapPointRepository extends MongoRepository<MapPoint, Long> {}
