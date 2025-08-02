package uk.gov.hmcts.reform.dev.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import uk.gov.hmcts.reform.dev.config.LocalDateTimeDeserialiser;

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

    @Column(name = "NAME")
    private String name;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @Column(name = "STATUS")
    private String status;

    @JsonDeserialize(using = LocalDateTimeDeserialiser.class)
    @Column(name = "CREATEDDATE")
    private LocalDateTime createdDate;
}
