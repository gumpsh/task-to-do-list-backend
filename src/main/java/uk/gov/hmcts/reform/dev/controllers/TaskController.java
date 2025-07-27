package uk.gov.hmcts.reform.dev.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getTasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateStatus(@PathVariable int id, @RequestParam String status) {
        return ResponseEntity.ok().body(taskService.updateTaskStatus(id, status));
    }
}
