package exam_portal.exam_portal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** Used by admin to create/update an exam. */
@Getter
@Setter
public class ExamRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @NotNull(message = "Passing marks is required")
    @Min(value = 0, message = "Passing marks cannot be negative")
    private Integer passingMarks;
}