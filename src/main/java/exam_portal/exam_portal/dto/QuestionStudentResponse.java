package exam_portal.exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * STUDENT-facing view of a question — deliberately has NO
 * correctOption field. This is the whole reason this DTO
 * exists separately from QuestionResponse.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionStudentResponse {

    private Long id;
    private String questionText;
    private Integer marks;
    private List<OptionResponse> options;
}