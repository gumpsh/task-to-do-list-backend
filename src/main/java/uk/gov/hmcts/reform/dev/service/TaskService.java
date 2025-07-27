package uk.gov.hmcts.reform.dev.service;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task getTask(int id) {
        return taskRepository.getTaskById(id);
    }

    public List<Task> getTasks() {
        return taskRepository.getAllTasks();
    }

    public Task updateTaskStatus(int id, String status) {
        Task task = taskRepository.getTaskById(id);

        if (task != null) {
            taskRepository.updateStatus(id, status);
        }

        return taskRepository.getTaskById(id);
    }

    public void deleteTask(int id) {
        taskRepository.deleteTask(id);
    }
}
