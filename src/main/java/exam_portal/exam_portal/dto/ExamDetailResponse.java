package exam_portal.exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * What a STUDENT sees before starting an exam (Section 9, Step 4
 * of the spec) — title, description, duration, question count,
 * total marks, passing marks. No questions or answers here yet.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDetailResponse {

    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private Integer numberOfQuestions;
    private Integer totalMarks;
    private Integer passingMarks;
}