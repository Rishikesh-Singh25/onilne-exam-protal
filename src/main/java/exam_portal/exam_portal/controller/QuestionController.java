package exam_portal.exam_portal.controller;

import exam_portal.exam_portal.dto.ApiResponse;
import exam_portal.exam_portal.dto.QuestionRequest;
import exam_portal.exam_portal.dto.QuestionResponse;
import exam_portal.exam_portal.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Two different base paths, both still under /api/admin/** so
 * SecurityConfig's existing ROLE_ADMIN rule covers both without
 * any changes there:
 *   /api/admin/exams/{examId}/questions   -> add / list
 *   /api/admin/questions/{questionId}     -> update / delete
 */
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/api/admin/exams/{examId}/questions")
    public ResponseEntity<ApiResponse<QuestionResponse>> addQuestion(
            @PathVariable Long examId, @Valid @RequestBody QuestionRequest request) {
        QuestionResponse response = questionService.addQuestion(examId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Question added successfully", response));
    }

    @GetMapping("/api/admin/exams/{examId}/questions")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByExam(@PathVariable Long examId) {
        List<QuestionResponse> questions = questionService.getQuestionsByExam(examId);
        return ResponseEntity.ok(ApiResponse.success("Questions fetched successfully", questions));
    }

    @PutMapping("/api/admin/questions/{questionId}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long questionId, @Valid @RequestBody QuestionRequest request) {
        QuestionResponse response = questionService.updateQuestion(questionId, request);
        return ResponseEntity.ok(ApiResponse.success("Question updated successfully", response));
    }

    @DeleteMapping("/api/admin/questions/{questionId}")
    public ResponseEntity<ApiResponse<Object>> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success("Question deleted successfully", null));
    }
}