package exam_portal.exam_portal.service;

import exam_portal.exam_portal.dto.AnswerRequest;
import exam_portal.exam_portal.dto.ExamDetailResponse;
import exam_portal.exam_portal.dto.PerformanceResponse;
import exam_portal.exam_portal.dto.ResultResponse;
import exam_portal.exam_portal.dto.StartExamResponse;



import java.util.List;

public interface StudentExamService {
    List<ExamDetailResponse> getAvailableExams();
    ExamDetailResponse getExamDetail(Long examId);
    StartExamResponse startExam(Long examId);
    void saveAnswer(Long attemptId, AnswerRequest request);
    ResultResponse submitExam(Long attemptId);

    // PHASE 11: new methods
    List<ResultResponse> getMyResults();
    ResultResponse getMyResultById(Long resultId);
    PerformanceResponse getMyPerformance();
}