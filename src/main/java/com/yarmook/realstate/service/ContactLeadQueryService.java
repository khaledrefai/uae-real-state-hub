package com.yarmook.realstate.service;

import com.yarmook.realstate.domain.ContactLead;
import com.yarmook.realstate.repository.ContactLeadRepository;
import com.yarmook.realstate.service.criteria.ContactLeadCriteria;
import com.yarmook.realstate.service.dto.ContactLeadDTO;
import com.yarmook.realstate.service.mapper.ContactLeadMapper;
import com.yarmook.realstate.service.util.FilterMatcher;
import com.yarmook.realstate.service.util.MongoPageUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * MongoDB-backed query service for {@link ContactLead}.
 */
@Service
public class ContactLeadQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactLeadQueryService.class);

    private final ContactLeadRepository contactLeadRepository;
    private final ContactLeadMapper contactLeadMapper;

    public ContactLeadQueryService(ContactLeadRepository contactLeadRepository, ContactLeadMapper contactLeadMapper) {
        this.contactLeadRepository = contactLeadRepository;
        this.contactLeadMapper = contactLeadMapper;
    }

    public Page<ContactLeadDTO> findByCriteria(ContactLeadCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        List<ContactLead> filtered = filterByCriteria(criteria);
        return MongoPageUtils.toPage(filtered, page).map(contactLeadMapper::toDto);
    }

    public long countByCriteria(ContactLeadCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        return filterByCriteria(criteria).size();
    }

    private List<ContactLead> filterByCriteria(ContactLeadCriteria criteria) {
        return contactLeadRepository.findAll().stream().filter(entity -> matchesCriteria(entity, criteria)).collect(Collectors.toList());
    }

    private boolean matchesCriteria(ContactLead entity, ContactLeadCriteria criteria) {
        if (criteria == null) {
            return true;
        }
        return (
            FilterMatcher.matchesRange(criteria.getId(), entity.getId()) &&
            FilterMatcher.matches(criteria.getName(), entity.getName()) &&
            FilterMatcher.matches(criteria.getEmail(), entity.getEmail()) &&
            FilterMatcher.matches(criteria.getPhone(), entity.getPhone()) &&
            FilterMatcher.matches(criteria.getWhatsapp(), entity.getWhatsapp()) &&
            FilterMatcher.matches(criteria.getMessage(), entity.getMessage()) &&
            FilterMatcher.matches(criteria.getSource(), entity.getSource()) &&
            FilterMatcher.matchesRange(criteria.getCreatedAt(), entity.getCreatedAt()) &&
            FilterMatcher.matches(criteria.getStatus(), entity.getStatus()) &&
            FilterMatcher.matchesRange(criteria.getSiteId(), entity.getSite() != null ? entity.getSite().getId() : null) &&
            FilterMatcher.matchesRange(criteria.getPropertyId(), entity.getProperty() != null ? entity.getProperty().getId() : null)
        );
    }
}
