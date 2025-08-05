package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.dto.TaskDataObject;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/search")
    public List<Task> searchUsers(@RequestParam Map<String, Object> filters) {
        return taskService.searchTasks(filters);
    }

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDataObject taskDataObject) {
        return ResponseEntity.ok().body(taskService.createTask(taskDataObject));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateStatus(@PathVariable int id, @Valid @RequestBody TaskDataObject taskDataObject) {
        return ResponseEntity.ok().body(taskService.updateTaskStatus(id, taskDataObject));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        return ResponseEntity.ok().body(taskService.deleteTask(id));
    }
}
