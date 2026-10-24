package exam_portal.exam_portal.controller;

import exam_portal.exam_portal.dto.*;
import exam_portal.exam_portal.service.StudentExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentExamController {

    private final StudentExamService studentExamService;

    @GetMapping("/exams")
    public ResponseEntity<ApiResponse<List<ExamDetailResponse>>> getAvailableExams() {
        List<ExamDetailResponse> exams = studentExamService.getAvailableExams();
        return ResponseEntity.ok(ApiResponse.success("Available exams fetched successfully", exams));
    }

    @GetMapping("/exams/{examId}")
    public ResponseEntity<ApiResponse<ExamDetailResponse>> getExamDetail(@PathVariable Long examId) {
        ExamDetailResponse exam = studentExamService.getExamDetail(examId);
        return ResponseEntity.ok(ApiResponse.success("Exam details fetched successfully", exam));
    }

    @PostMapping("/exams/{examId}/start")
    public ResponseEntity<ApiResponse<StartExamResponse>> startExam(@PathVariable Long examId) {
        StartExamResponse response = studentExamService.startExam(examId);
        return ResponseEntity.ok(ApiResponse.success("Exam started successfully", response));
    }

    @PostMapping("/attempts/{attemptId}/answers")
    public ResponseEntity<ApiResponse<Object>> saveAnswer(
            @PathVariable Long attemptId, @Valid @RequestBody AnswerRequest request) {
        studentExamService.saveAnswer(attemptId, request);
        return ResponseEntity.ok(ApiResponse.success("Answer saved successfully", null));
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<ApiResponse<ResultResponse>> submitExam(@PathVariable Long attemptId) {
        ResultResponse response = studentExamService.submitExam(attemptId);
        return ResponseEntity.ok(ApiResponse.success("Exam submitted and graded successfully", response));
    }

    // PHASE 11: new endpoints
    @GetMapping("/results")
    public ResponseEntity<ApiResponse<List<ResultResponse>>> getMyResults() {
        List<ResultResponse> results = studentExamService.getMyResults();
        return ResponseEntity.ok(ApiResponse.success("Results fetched successfully", results));
    }

    @GetMapping("/results/{resultId}")
    public ResponseEntity<ApiResponse<ResultResponse>> getMyResultById(@PathVariable Long resultId) {
        ResultResponse result = studentExamService.getMyResultById(resultId);
        return ResponseEntity.ok(ApiResponse.success("Result fetched successfully", result));
    }

    @GetMapping("/performance")
    public ResponseEntity<ApiResponse<PerformanceResponse>> getMyPerformance() {
        PerformanceResponse performance = studentExamService.getMyPerformance();
        return ResponseEntity.ok(ApiResponse.success("Performance fetched successfully", performance));
    }
}