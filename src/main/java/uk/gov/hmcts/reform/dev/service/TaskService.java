package uk.gov.hmcts.reform.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.config.ErrorHandler;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task getTask(int id) {
        return taskRepository.findById(id).isPresent() ? taskRepository.findById(id).get() : null;
    }

    public List<Task> getTasks() {

        List<Task> tasks = taskRepository.findAll();

        // Sort in descending created date order
        tasks.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));

        return tasks;
    }

    public Task createTask(Task task) {

        if (!getTasks().contains(task)) {
            task.setCreatedDate(LocalDateTime.now());

            return taskRepository.save(task);
        } else {
            return task;
        }
    }

    public Task saveTask(Task task){

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(int id, String status) {
        Task task = taskRepository.findById(id).isPresent() ? taskRepository.findById(id).get() : null;

        if (task != null) {
            task.setStatus(status);
        }

        saveTask(task);
        return task;
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
