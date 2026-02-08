package task.todoList.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "hmcts-dev-test")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UIS")
    @SequenceGenerator(sequenceName = "hmcts-dev-test.users_id_seq", name = "UIS", allocationSize = 1)
    private int id;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
