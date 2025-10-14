package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.AgentSite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AgentSite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentSiteRepository extends MongoRepository<AgentSite, Long> {}
