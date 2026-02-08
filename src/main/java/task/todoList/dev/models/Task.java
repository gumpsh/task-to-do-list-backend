package task.todoList.dev.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASK", schema = "hmcts-dev-test")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIS")
    @SequenceGenerator(sequenceName = "hmcts-dev-test.task_id_seq", name = "TIS", allocationSize = 1)
    private int id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DUEDATE")
    private LocalDateTime dueDate;
}
