package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.dto.ExamRequest;
import exam_portal.exam_portal.dto.ExamResponse;
import exam_portal.exam_portal.entity.Exam;
import exam_portal.exam_portal.entity.ExamStatus;
import exam_portal.exam_portal.exception.ResourceNotFoundException;
import exam_portal.exam_portal.repository.ExamRepository;
import exam_portal.exam_portal.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    @Override
    public ExamResponse createExam(ExamRequest request) {
        Exam exam = Exam.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .passingMarks(request.getPassingMarks())
                .totalMarks(0) // no questions yet — recalculated in Phase 8
                .status(ExamStatus.DRAFT)
                .build();

        Exam saved = examRepository.save(exam);
        return toResponse(saved);
    }

    @Override
    public List<ExamResponse> getAllExams() {
        return examRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ExamResponse getExamById(Long id) {
        return toResponse(findExamOrThrow(id));
    }

    @Override
    public ExamResponse updateExam(Long id, ExamRequest request) {
        Exam exam = findExamOrThrow(id);

        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDuration(request.getDuration());
        exam.setPassingMarks(request.getPassingMarks());
        // totalMarks and status are intentionally left untouched here —
        // they're managed by question changes and publish/unpublish only.

        return toResponse(examRepository.save(exam));
    }

    @Override
    public void deleteExam(Long id) {
        Exam exam = findExamOrThrow(id);
        examRepository.delete(exam);
    }

    @Override
    public ExamResponse publishExam(Long id) {
        Exam exam = findExamOrThrow(id);
        exam.setStatus(ExamStatus.PUBLISHED);
        return toResponse(examRepository.save(exam));
    }

    @Override
    public ExamResponse unpublishExam(Long id) {
        Exam exam = findExamOrThrow(id);
        exam.setStatus(ExamStatus.DRAFT);
        return toResponse(examRepository.save(exam));
    }

    // ---- helpers ----

    private Exam findExamOrThrow(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + id));
    }

    private ExamResponse toResponse(Exam exam) {
        return ExamResponse.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .duration(exam.getDuration())
                .totalMarks(exam.getTotalMarks())
                .passingMarks(exam.getPassingMarks())
                .status(exam.getStatus())
                .createdAt(exam.getCreatedAt())
                .build();
    }
}