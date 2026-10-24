package exam_portal.exam_portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Used by POST /api/student/attempts/{attemptId}/answers */
@Getter
@Setter
public class AnswerRequest {

    @NotNull(message = "Question id is required")
    private Long questionId;

    @NotBlank(message = "Selected option is required")
    private String selectedOption;
}