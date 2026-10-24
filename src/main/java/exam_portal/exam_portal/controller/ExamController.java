package exam_portal.exam_portal.controller;

import exam_portal.exam_portal.dto.ApiResponse;
import exam_portal.exam_portal.dto.ExamRequest;
import exam_portal.exam_portal.dto.ExamResponse;
import exam_portal.exam_portal.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here sit under /api/admin/exams, which SecurityConfig
 * (Phase 6) already restricts to ROLE_ADMIN — this controller doesn't
 * need to check the role itself.
 */
@RestController
@RequestMapping("/api/admin/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamResponse>> createExam(@Valid @RequestBody ExamRequest request) {
        ExamResponse response = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Exam created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamResponse>>> getAllExams() {
        List<ExamResponse> exams = examService.getAllExams();
        return ResponseEntity.ok(ApiResponse.success("Exams fetched successfully", exams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> getExamById(@PathVariable Long id) {
        ExamResponse response = examService.getExamById(id);
        return ResponseEntity.ok(ApiResponse.success("Exam fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExamResponse>> updateExam(
            @PathVariable Long id, @Valid @RequestBody ExamRequest request) {
        ExamResponse response = examService.updateExam(id, request);
        return ResponseEntity.ok(ApiResponse.success("Exam updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.ok(ApiResponse.success("Exam deleted successfully", null));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<ExamResponse>> publishExam(@PathVariable Long id) {
        ExamResponse response = examService.publishExam(id);
        return ResponseEntity.ok(ApiResponse.success("Exam published successfully", response));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<ApiResponse<ExamResponse>> unpublishExam(@PathVariable Long id) {
        ExamResponse response = examService.unpublishExam(id);
        return ResponseEntity.ok(ApiResponse.success("Exam unpublished successfully", response));
    }
}