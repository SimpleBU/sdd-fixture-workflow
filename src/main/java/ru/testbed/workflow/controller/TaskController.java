package ru.testbed.workflow.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.testbed.workflow.dto.CommentDto;
import ru.testbed.workflow.dto.TaskDto;
import ru.testbed.workflow.dto.TaskSearchRequest;
import ru.testbed.workflow.dto.VariableDto;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("taskId") String taskId) {
        return ResponseEntity.ok(new TaskDto());
    }

    @PostMapping("/search")
    public ResponseEntity<List<TaskDto>> searchTasks(@RequestBody TaskSearchRequest query) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Void> completeTask(
            @PathVariable("taskId") String taskId,
            @RequestBody Map<String, Object> variables) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}/variables")
    public ResponseEntity<List<VariableDto>> getTaskVariables(@PathVariable("taskId") String taskId) {
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{taskId}/variables/{variableName}")
    public ResponseEntity<Void> updateTaskVariable(
            @PathVariable("taskId") String taskId,
            @PathVariable("variableName") String variableName,
            @RequestBody VariableDto variable) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable("taskId") String taskId,
            @RequestBody CommentDto comment) {
        return ResponseEntity.ok(new CommentDto());
    }

}
