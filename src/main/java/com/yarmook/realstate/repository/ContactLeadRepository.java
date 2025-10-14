package com.yarmook.realstate.repository;

import com.yarmook.realstate.domain.ContactLead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ContactLead entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactLeadRepository extends MongoRepository<ContactLead, Long> {}
