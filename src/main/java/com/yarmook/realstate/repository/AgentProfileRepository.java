package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.AgentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AgentProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentProfileRepository extends MongoRepository<AgentProfile, Long> {}
