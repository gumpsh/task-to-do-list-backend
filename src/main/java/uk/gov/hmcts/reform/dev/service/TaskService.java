package uk.gov.hmcts.reform.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.config.EntityNotFoundException;
import uk.gov.hmcts.reform.dev.config.ErrorHandler;
import uk.gov.hmcts.reform.dev.config.InvalidStatusException;
import uk.gov.hmcts.reform.dev.dto.TaskDataObject;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task getTask(int id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task With ID: " + id + " Not Found"));
    }

    public List<Task> getTasks() {

        List<Task> tasks = taskRepository.findAll();

        // Sort in descending created date order
        tasks.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));

        return tasks;
    }

    public Task createTask(TaskDataObject taskDataObject) {

        Task task = new Task();
        task.setName(taskDataObject.getName());
        task.setTitle(taskDataObject.getTitle());
        task.setDescription(taskDataObject.getDescription());
        task.setStatus(taskDataObject.getStatus());
        task.setCreatedDate(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public enum Status {
        CREATED("Created"),
        STARTED("Started"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        public final String label;

        private Status(String label) {
            this.label = label;
        }
    }

    public Task saveTask(Task task){

        List<Status> matches = Arrays.stream(Status.values()).filter((s) -> s.label.equals(task.getStatus())).collect(Collectors.toList());

        if (matches.isEmpty()) {
            throw new InvalidStatusException(task.getStatus() + " Is An Invalid Status");
        }

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(int id, TaskDataObject taskDataObject) {
        Task task = getTask(id);

        if (task != null) {
            task.setStatus(taskDataObject.getStatus());
        }

        return saveTask(task);
    }

    public Boolean deleteTask(int id) {
        Optional task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.delete((Task) task.get());
            return true;
        }

        return false;
    }
}
