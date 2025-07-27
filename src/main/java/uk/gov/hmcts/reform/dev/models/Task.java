package uk.gov.hmcts.reform.dev.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    private int id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdDate;
}
