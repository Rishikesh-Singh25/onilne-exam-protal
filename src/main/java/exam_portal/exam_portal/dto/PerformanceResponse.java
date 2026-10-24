package exam_portal.exam_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** Returned by GET /api/student/performance — a summary across ALL
 *  of the current student's completed attempts. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceResponse {

    private int totalExamsAttempted;
    private int totalPassed;
    private int totalFailed;
    private double averagePercentage;
    private double bestPercentage;
    private List<ResultResponse> results; // full history, most recent logic left to the frontend to sort/display
}