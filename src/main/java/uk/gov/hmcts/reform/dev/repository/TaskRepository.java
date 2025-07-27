package uk.gov.hmcts.reform.dev.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.dev.models.Task;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Query("SELECT * FROM task WHERE id = :id")
    public Task getTaskById(int id);

    @Query("SELECT * FROM task")
    public List<Task> getAllTasks();

    @Query("UPDATE task SET status = :status WHERE id = :id")
    public Task updateStatus(int id, String status);

    @Query("DELETE FROM task where id = :id")
    public void deleteTask(int id);
}
