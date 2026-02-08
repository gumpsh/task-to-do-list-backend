package task.todoList.dev;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import task.todoList.dev.dto.TaskDataObject;
import task.todoList.dev.exception.EntityNotFoundException;
import task.todoList.dev.models.Task;
import task.todoList.dev.controllers.TaskController;
import task.todoList.dev.service.TaskService;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;


@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getTaskByIdReturnsTaskWhenExists() {
        Task task = new Task();
        task.setId(1);
        when(taskService.getTask(1)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTask(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(task, response.getBody());
    }

    @Test
    void getTaskByIdThrowsExceptionWhenNotFound() {
        when(taskService.getTask(1)).thenThrow(new EntityNotFoundException("Task not found"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskController.getTask(1));
    }

    @Test
    void getAllTasksReturnsListOfTasks() {
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskService.getTasks()).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(tasks, response.getBody());
    }

    @Test
    void createTaskReturnsCreatedTask() {
        TaskDataObject taskDataObject = new TaskDataObject();
        Task task = new Task();
        when(taskService.createTask(taskDataObject)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(taskDataObject);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(task, response.getBody());
    }

    @Test
    void updateTaskStatusUpdatesAndReturnsTask() {
        TaskDataObject taskDataObject = new TaskDataObject();
        Task task = new Task();
        when(taskService.updateTaskStatus(1, taskDataObject)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateStatus(1, taskDataObject);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(task, response.getBody());
    }

    @Test
    void deleteTaskDeletesTaskSuccessfully() {
        doNothing().when(taskService).deleteTask(1);

        ResponseEntity response = taskController.delete(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1);
    }

    @Test
    void fetchAllStatusesReturnsListOfStatuses() {
        List<String> statuses = List.of("Created", "Started", "Completed", "Cancelled");
        when(taskService.fetchAllStatuses()).thenReturn(statuses);

        ResponseEntity<List<String>> response = taskController.getStatuses();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(statuses, response.getBody());
    }

    @Test
    void fetchBySuppliedSearchCriteria() {

        List<Task> tasks = List.of(new Task(), new Task());

        Map<String, Object> filters = Map.of(
            "startDate", "2025-01-01T00:00:00",
            "endDate", "2025-12-31T23:59:59",
            "status", TaskService.Status.CREATED.label
        );

        when(taskService.searchTasks(filters)).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.searchTasks(filters);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(tasks, response.getBody());
    }
}
