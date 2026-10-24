package exam_portal.exam_portal.service;

import exam_portal.exam_portal.dto.ExamRequest;
import exam_portal.exam_portal.dto.ExamResponse;

import java.util.List;

public interface ExamService {
    ExamResponse createExam(ExamRequest request);
    List<ExamResponse> getAllExams();
    ExamResponse getExamById(Long id);
    ExamResponse updateExam(Long id, ExamRequest request);
    void deleteExam(Long id);
    ExamResponse publishExam(Long id);
    ExamResponse unpublishExam(Long id);
}