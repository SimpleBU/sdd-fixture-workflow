package ru.testbed.workflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.IntegrationSettingsDto;

/** Ловушки: dataSource, database, contentType, metadataVersion. */
@RestController
@RequestMapping("/api/integrations")
public class IntegrationController {

    @GetMapping("/{integrationId}/settings")
    public ResponseEntity<IntegrationSettingsDto> getSettings(@PathVariable("integrationId") String integrationId) {
        return ResponseEntity.ok(new IntegrationSettingsDto());
    }

    @PutMapping("/{integrationId}/settings")
    public ResponseEntity<Void> updateSettings(
            @PathVariable("integrationId") String integrationId,
            @RequestBody IntegrationSettingsDto settings) {
        return ResponseEntity.noContent().build();
    }

}
