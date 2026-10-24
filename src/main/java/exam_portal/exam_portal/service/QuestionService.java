package exam_portal.exam_portal.service;

import exam_portal.exam_portal.dto.QuestionRequest;
import exam_portal.exam_portal.dto.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse addQuestion(Long examId, QuestionRequest request);
    List<QuestionResponse> getQuestionsByExam(Long examId);
    QuestionResponse updateQuestion(Long questionId, QuestionRequest request);
    void deleteQuestion(Long questionId);
}