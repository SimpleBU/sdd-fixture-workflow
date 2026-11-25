package ru.testbed.workflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.FormSubmissionDto;

@RestController
@RequestMapping("/api/forms/{formId}/submissions")
public class FormController {

    @PostMapping
    public ResponseEntity<Void> submit(
            @PathVariable("formId") String formId,
            @RequestBody FormSubmissionDto submission) {
        return ResponseEntity.noContent().build();
    }

}
