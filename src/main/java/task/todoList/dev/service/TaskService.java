package task.todoList.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import task.todoList.dev.dto.TaskDataObject;
import task.todoList.dev.exception.EntityNotFoundException;
import task.todoList.dev.exception.InvalidStatusException;
import task.todoList.dev.models.Task;
import task.todoList.dev.repository.TaskRepository;
import task.todoList.dev.search.TaskSpecification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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

        // Sort by due date
        tasks.sort(Comparator.comparing(Task::getDueDate));

        return tasks;
    }

    public List<Task> searchTasks(Map<String, Object> filters) {
        Map<String, Object> parsedFilters = new HashMap<>();

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

        List<Task> tasks = taskRepository.findAll(new TaskSpecification<>(parsedFilters));

        // Sort by due date
        tasks.sort(Comparator.comparing(Task::getDueDate));

        return tasks;
    }

    public Task createTask(TaskDataObject taskDataObject) {

        Task task = new Task();
        task.setTitle(taskDataObject.getTitle());
        task.setDescription(taskDataObject.getDescription());
        task.setStatus(taskDataObject.getStatus() != null ? taskDataObject.getStatus() : Status.CREATED.label);
        task.setDueDate(taskDataObject.getDueDate());

        return taskRepository.save(task);
    }

    public Task saveTask(Task task){

        List<Status> matchingStatuses = Arrays.stream(Status.values()).filter((s) -> s.label.equals(task.getStatus())).collect(Collectors.toList());

        if (matchingStatuses.isEmpty()) {
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
