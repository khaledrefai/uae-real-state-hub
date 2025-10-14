package com.yarmook.realstate.web.rest;

import com.yarmook.realstate.service.PropertyImportService;
import com.yarmook.realstate.service.PropertyImportService.PropertyRefreshResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administrative endpoints for orchestrating manual imports from Reely.
 */
@RestController
@RequestMapping("/api/admin/reely")
public class ReelyAdminResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReelyAdminResource.class);

    private final PropertyImportService propertyImportService;

    public ReelyAdminResource(PropertyImportService propertyImportService) {
        this.propertyImportService = propertyImportService;
    }

    /**
     * {@code POST /api/admin/reely/import} : Trigger a manual sync against the Reely API.
     *
     * @return a summary of the refresh operation.
     */
    @PostMapping("/import")
    public ResponseEntity<PropertyRefreshResult> triggerImport() {
        LOG.info("Manual Reely import requested by administrator");
        PropertyRefreshResult result = propertyImportService.refreshFromExternal();
        return ResponseEntity.ok(result);
    }
}
