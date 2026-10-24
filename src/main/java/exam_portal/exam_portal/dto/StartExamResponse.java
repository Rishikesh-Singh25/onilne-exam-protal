package exam_portal.exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/** Returned by POST /api/student/exams/{examId}/start */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartExamResponse {

    private Long attemptId;
    private Long examId;
    private String examTitle;
    private LocalDateTime startTime;
    private Integer durationInMinutes;
    private List<QuestionStudentResponse> questions;
}