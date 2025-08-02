package uk.gov.hmcts.reform.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

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

        tasks.sort((o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()));

        return tasks;
    }

    public Task saveTask(Task task){

        Task savedTask = taskRepository.save(task);

        return savedTask;
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
