package uk.gov.hmcts.reform.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDataObject {

    @NotBlank(message = "Title Must Be Provided")
    private String title;
    private String description;
    @NotBlank(message = "Status Must Be Provided")
    private String status;
    @NotNull(message = "Due Date Must Be Provided")
    private LocalDateTime dueDate;
}
