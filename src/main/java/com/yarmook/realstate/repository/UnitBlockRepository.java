package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.UnitBlock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the UnitBlock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitBlockRepository extends MongoRepository<UnitBlock, Long> {}
