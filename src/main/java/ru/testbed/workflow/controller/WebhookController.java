package ru.testbed.workflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.WebhookEventDto;

@RestController
@RequestMapping("/hooks/{source}")
public class WebhookController {

    @PostMapping
    public ResponseEntity<Void> receive(
            @PathVariable("source") String source,
            @RequestHeader(name = "X-Hub-Signature", required = false) String xHubSignature,
            @RequestBody WebhookEventDto event) {
        return ResponseEntity.noContent().build();
    }

}
