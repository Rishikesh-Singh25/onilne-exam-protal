package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.dto.AdminResultResponse;
import exam_portal.exam_portal.entity.Result;
import exam_portal.exam_portal.exception.ResourceNotFoundException;
import exam_portal.exam_portal.repository.ExamRepository;
import exam_portal.exam_portal.repository.ResultRepository;
import exam_portal.exam_portal.repository.UserRepository;
import exam_portal.exam_portal.service.AdminResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminResultServiceImpl implements AdminResultService {

    private final ResultRepository resultRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    @Override
    public List<AdminResultResponse> getAllResults() {
        return resultRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<AdminResultResponse> getResultsByExam(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with id: " + examId);
        }
        return resultRepository.findByAttempt_Exam_Id(examId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<AdminResultResponse> getResultsByStudent(Long studentId) {
        if (!userRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return resultRepository.findByAttempt_Student_Id(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AdminResultResponse toResponse(Result result) {
        return AdminResultResponse.builder()
                .id(result.getId())
                .attemptId(result.getAttempt().getId())
                .studentId(result.getAttempt().getStudent().getId())
                .studentName(result.getAttempt().getStudent().getName())
                .studentEmail(result.getAttempt().getStudent().getEmail())
                .examId(result.getAttempt().getExam().getId())
                .examTitle(result.getAttempt().getExam().getTitle())
                .totalQuestions(result.getTotalQuestions())
                .correctAnswers(result.getCorrectAnswers())
                .wrongAnswers(result.getWrongAnswers())
                .totalMarks(result.getTotalMarks())
                .marksObtained(result.getMarksObtained())
                .percentage(result.getPercentage())
                .resultStatus(result.getResultStatus())
                .createdAt(result.getCreatedAt())
                .build();
    }
}