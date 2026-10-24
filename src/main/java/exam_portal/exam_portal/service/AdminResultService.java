package exam_portal.exam_portal.service;

import exam_portal.exam_portal.dto.AdminResultResponse;

import java.util.List;

public interface AdminResultService {
    List<AdminResultResponse> getAllResults();
    List<AdminResultResponse> getResultsByExam(Long examId);
    List<AdminResultResponse> getResultsByStudent(Long studentId);
}