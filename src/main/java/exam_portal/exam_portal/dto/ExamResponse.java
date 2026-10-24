package exam_portal.exam_portal.dto;

import exam_portal.exam_portal.entity.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Admin-facing view of an exam — includes status and full internals. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {

    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private Integer totalMarks;
    private Integer passingMarks;
    private ExamStatus status;
    private LocalDateTime createdAt;
}