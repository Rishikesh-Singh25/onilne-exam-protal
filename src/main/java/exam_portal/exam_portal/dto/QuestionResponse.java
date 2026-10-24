package exam_portal.exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * ADMIN-ONLY view of a question — includes correctOption.
 * NEVER return this DTO from a /api/student/** endpoint.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long id;
    private String questionText;
    private Integer marks;
    private String correctOption;
    private List<OptionResponse> options;
}