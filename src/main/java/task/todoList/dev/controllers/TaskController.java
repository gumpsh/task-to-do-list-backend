package task.todoList.dev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.todoList.dev.dto.TaskDataObject;
import task.todoList.dev.models.Task;
import task.todoList.dev.service.TaskService;
import java.util.List;
import java.util.Map;

@CrossOrigin()
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "Task Management API")
public class TaskController {

    private final TaskService taskService;

    // Fetch a task by ID
    @Operation(summary = "Fetch a task By id", description = "Retrieve details for a single task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Task Found",
            content = @Content(schema = @Schema(implementation = TaskDataObject.class))),
        @ApiResponse(responseCode = "404",
            description = "Not found",
            content = @Content(schema = @Schema)
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    // Fetch all tasks
    @Operation(summary = "Fetch all tasks", description = "Retrieve a list of all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Tasks successfully retrieved",
            content = @Content(schema = @Schema(implementation = TaskDataObject.class))),
        @ApiResponse(responseCode = "404",
            description = "Not found",
            content = @Content(schema = @Schema)
        )
    })
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getTasks());
    }

    // Search tasks by criteria
    @Operation(
        summary = "Fetch tasks by search criteria", description = "Returns a list of task that match the supplied criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Tasks successfully retrieved",
            content = @Content(schema = @Schema(implementation = TaskDataObject.class))),
        @ApiResponse(responseCode = "400",
            description = "Bad request",
            content = @Content(schema = @Schema)
        )
    })
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam Map<String, Object> filters) {
        return ResponseEntity.ok().body(taskService.searchTasks(filters));
    }

    // Create a new task
    @Operation(summary = "Create a task", description = "Creates a new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Task successfully created",
            content = @Content(schema = @Schema(implementation = TaskDataObject.class))),
        @ApiResponse(responseCode = "400",
            description = "Request has missing data",
            content = @Content(schema = @Schema)
        )
    })
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDataObject taskDataObject) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskDataObject));
    }

    // Update a task's status
    @Operation(summary = "Update status", description = "Updates a task's status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Task successfully updated",
            content = @Content(schema = @Schema(implementation = TaskDataObject.class))),
        @ApiResponse(responseCode = "400",
            description = "Request has missing data",
            content = @Content(schema = @Schema)
        )
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateStatus(@PathVariable int id, @Valid @RequestBody TaskDataObject taskDataObject) {
        return ResponseEntity.ok().body(taskService.updateTaskStatus(id, taskDataObject));
    }

    // Delete a task
    @Operation(summary = "Deletes a task", description = "Searches for a task by id and deletes it if found")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Task successfully deleted",
            content = @Content(schema = @Schema(implementation = TaskDataObject.class))),
        @ApiResponse(responseCode = "404",
            description = "Task not found",
            content = @Content(schema = @Schema)
        )
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch all allowed statuses
    @Operation(summary = "Fetch Statuses", description = "Provide Allowed Statuses To Front End")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Retrieved Statuses",
            content = @Content(
                schema = @Schema(
                    implementation = TaskService.Status.class),
                        examples = @ExampleObject(
                            value = "{\n" +
                                "  \"Created\", \n" +
                                "  \"Started\", \n" +
                                "  \"Completed\", \n" +
                                "  \"Cancelled\" \n" +
                                "}"))),

    })
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getStatuses() {
        return ResponseEntity.ok().body(taskService.fetchAllStatuses());
    }
}
