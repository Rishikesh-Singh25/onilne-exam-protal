package exam_portal.exam_portal.dto;

import exam_portal.exam_portal.entity.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Admin-facing view of a result — includes WHO it belongs to,
 * since an admin is looking across many students at once.
 * A student never sees this DTO; they get ResultResponse instead.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResultResponse {

    private Long id;
    private Long attemptId;

    private Long studentId;
    private String studentName;
    private String studentEmail;

    private Long examId;
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