package exam_portal.exam_portal.controller;

import exam_portal.exam_portal.dto.AdminResultResponse;
import exam_portal.exam_portal.dto.ApiResponse;
import exam_portal.exam_portal.service.AdminResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All three paths already fall under /api/admin/**, so
 * SecurityConfig's ROLE_ADMIN rule from Phase 6 covers them
 * with no changes needed there.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminResultController {

    private final AdminResultService adminResultService;

    @GetMapping("/results")
    public ResponseEntity<ApiResponse<List<AdminResultResponse>>> getAllResults() {
        List<AdminResultResponse> results = adminResultService.getAllResults();
        return ResponseEntity.ok(ApiResponse.success("All results fetched successfully", results));
    }

    @GetMapping("/exams/{examId}/results")
    public ResponseEntity<ApiResponse<List<AdminResultResponse>>> getResultsByExam(@PathVariable Long examId) {
        List<AdminResultResponse> results = adminResultService.getResultsByExam(examId);
        return ResponseEntity.ok(ApiResponse.success("Exam results fetched successfully", results));
    }

    @GetMapping("/students/{studentId}/results")
    public ResponseEntity<ApiResponse<List<AdminResultResponse>>> getResultsByStudent(@PathVariable Long studentId) {
        List<AdminResultResponse> results = adminResultService.getResultsByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student results fetched successfully", results));
    }
}