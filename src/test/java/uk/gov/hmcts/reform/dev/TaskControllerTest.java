package uk.gov.hmcts.reform.dev;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.dev.exception.EntityNotFoundException;
import uk.gov.hmcts.reform.dev.controllers.TaskController;
import uk.gov.hmcts.reform.dev.dto.TaskDataObject;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.service.TaskService;
import java.util.List;
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
}
