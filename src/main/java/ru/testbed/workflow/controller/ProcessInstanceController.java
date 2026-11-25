package ru.testbed.workflow.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.ProcessStartRequest;

@RestController
@RequestMapping("/v1/process-instances")
public class ProcessInstanceController {

    @PostMapping
    public ResponseEntity<Void> startProcess(@RequestBody ProcessStartRequest start) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{processInstanceId}/variables")
    public ResponseEntity<Map<String, Object>> getProcessVariables(@PathVariable("processInstanceId") String processInstanceId) {
        return ResponseEntity.ok(Map.of());
    }

}
