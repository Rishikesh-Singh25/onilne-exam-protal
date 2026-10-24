package exam_portal.exam_portal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Used by admin to add/update a question.
 * "options" is a plain list of option text in order — the service
 * layer assigns labels A, B, C, D... based on position.
 * "correctOption" must match one of those generated labels.
 */
@Getter
@Setter
public class QuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Marks is required")
    @Min(value = 1, message = "Marks must be at least 1")
    private Integer marks;

    @NotBlank(message = "Correct option is required")
    private String correctOption;

    @NotNull(message = "Options are required")
    @Size(min = 2, message = "At least 2 options are required")
    private List<String> options;
}