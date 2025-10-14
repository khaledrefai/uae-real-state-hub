package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.service.PropertyQueryService;
import com.yarmook.realstate.service.PropertyService;
import com.yarmook.realstate.service.criteria.PropertyCriteria;
import com.yarmook.realstate.service.dto.PropertyDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/public")
public class PublicPropertyResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicPropertyResource.class);

    private final PropertyService propertyService;
    private final PropertyQueryService propertyQueryService;

    public PublicPropertyResource(PropertyService propertyService, PropertyQueryService propertyQueryService) {
        this.propertyService = propertyService;
        this.propertyQueryService = propertyQueryService;
    }

    /**
     * {@code GET  /public/properties}: get properties without authentication.
     */
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyDTO>> getAllProperties(
        PropertyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("Public request to get Properties by criteria: {}", criteria);
        Page<PropertyDTO> page = propertyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /public/properties/:id}: get a single property without authentication.
     */
    @GetMapping("/properties/{id}")
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable("id") Long id) {
        LOG.debug("Public request to get Property : {}", id);
        Optional<PropertyDTO> propertyDTO = propertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propertyDTO);
    }
}
