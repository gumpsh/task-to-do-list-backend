package uk.gov.hmcts.reform.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDataObject {

    @NotBlank(message = "Title Must Be Provided")
    private String title;
    private String description;
    private String status = "Created";
    @NotNull(message = "Due Date Must Be Provided")
    private LocalDateTime dueDate;
}
