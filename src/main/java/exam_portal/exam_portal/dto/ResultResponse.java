package exam_portal.exam_portal.dto;

import exam_portal.exam_portal.entity.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Returned after exam submission, and via GET /api/student/results/{id} */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {

    private Long id;
    private Long attemptId;
    private String examTitle;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer totalMarks;
    private Integer marksObtained;
    private Double percentage;
    private ResultStatus resultStatus;
    private LocalDateTime createdAt;
}