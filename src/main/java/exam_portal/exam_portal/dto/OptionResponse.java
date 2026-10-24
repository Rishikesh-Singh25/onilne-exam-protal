package exam_portal.exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Safe to show to both admin and student — no correctness info here. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionResponse {

    private Long id;
    private String optionLabel;
    private String optionText;
}