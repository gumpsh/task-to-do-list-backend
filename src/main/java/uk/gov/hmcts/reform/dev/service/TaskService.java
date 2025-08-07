package uk.gov.hmcts.reform.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.config.EntityNotFoundException;
import uk.gov.hmcts.reform.dev.config.ErrorHandler;
import uk.gov.hmcts.reform.dev.config.InvalidStatusException;
import uk.gov.hmcts.reform.dev.dto.TaskDataObject;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.search.TaskSpecification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public enum Status {
        CREATED("Created"),
        STARTED("Started"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        public final String label;

        Status(String label) {
            this.label = label;
        }
    }

    public Task getTask(int id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task With ID: " + id + " Not Found"));
    }

    public List<Task> getTasks() {

        List<Task> tasks = taskRepository.findAll();

        // Sort in descending created date order
        tasks.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));

        return tasks;
    }

    public List<Task> searchTasks(Map<String, Object> filters) {
        Map<String, Object> parsedFilters = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                if (key.equals("startDate") || key.equals("endDate")) {
                    try {
                        value = LocalDateTime.parse((String) value, formatter);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid date format for: " + key);
                    }
                }

                parsedFilters.put(key, value);
            }
        }

        return taskRepository.findAll(new TaskSpecification<>(parsedFilters));
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

    public void deleteTask(int id) {
        taskRepository.delete(getTask(id));
    }

    public List<String> fetchAllStatuses() {

        List<String> statuses = Arrays.stream(Status.values()).map(v -> v.label).collect(Collectors.toList());

        return statuses;
    }
}
