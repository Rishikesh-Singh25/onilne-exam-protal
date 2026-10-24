package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.dto.*;
import exam_portal.exam_portal.entity.*;
import exam_portal.exam_portal.exception.ExamAlreadySubmittedException;
import exam_portal.exam_portal.exception.InvalidExamException;
import exam_portal.exam_portal.exception.ResourceNotFoundException;
import exam_portal.exam_portal.repository.*;
import exam_portal.exam_portal.security.CurrentUserProvider;
import exam_portal.exam_portal.service.ExamTimerService;
import exam_portal.exam_portal.service.GradingService;
import exam_portal.exam_portal.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentExamServiceImpl implements StudentExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final AnswerRepository answerRepository;
    private final ResultRepository resultRepository;
    private final CurrentUserProvider currentUserProvider;
    private final GradingService gradingService;
    private final ExamTimerService examTimerService; // PHASE 12: new dependency

    @Override
    public List<ExamDetailResponse> getAvailableExams() {
        return examRepository.findByStatus(ExamStatus.PUBLISHED)
                .stream()
                .map(this::toExamDetailResponse)
                .toList();
    }

    @Override
    public ExamDetailResponse getExamDetail(Long examId) {
        Exam exam = findPublishedExamOrThrow(examId);
        return toExamDetailResponse(exam);
    }

    @Override
    public StartExamResponse startExam(Long examId) {
        User student = currentUserProvider.getCurrentUser();
        Exam exam = findPublishedExamOrThrow(examId);

        ExamAttempt attempt = examAttemptRepository
                .findByStudentIdAndExamIdAndStatus(student.getId(), examId, AttemptStatus.IN_PROGRESS)
                .orElseGet(() -> examAttemptRepository.save(
                        ExamAttempt.builder()
                                .student(student)
                                .exam(exam)
                                .startTime(LocalDateTime.now())
                                .status(AttemptStatus.IN_PROGRESS)
                                .build()
                ));

        List<QuestionStudentResponse> questions = questionRepository.findByExamId(examId)
                .stream()
                .map(this::toStudentQuestionResponse)
                .toList();

        return StartExamResponse.builder()
                .attemptId(attempt.getId())
                .examId(exam.getId())
                .examTitle(exam.getTitle())
                .startTime(attempt.getStartTime())
                .durationInMinutes(exam.getDuration())
                .questions(questions)
                .build();
    }

    @Override
    public void saveAnswer(Long attemptId, AnswerRequest request) {
        User student = currentUserProvider.getCurrentUser();

        ExamAttempt attempt = examAttemptRepository.findByIdAndStudentId(attemptId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            throw new ExamAlreadySubmittedException(
                    "This exam attempt has already been submitted; answers can no longer be changed");
        }

        // PHASE 12: reject new/changed answers once time is up. We don't
        // auto-grade here — that only happens when submit() is called,
        // which the frontend's countdown timer will trigger automatically
        // client-side, with this check as the authoritative backstop.
        if (examTimerService.isTimeExpired(attempt)) {
            throw new ExamAlreadySubmittedException(
                    "Time is up for this exam attempt; no further answers can be saved. Please submit.");
        }

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + request.getQuestionId()));

        if (!question.getExam().getId().equals(attempt.getExam().getId())) {
            throw new InvalidExamException("This question does not belong to the exam being attempted");
        }

        Answer answer = answerRepository.findByAttemptIdAndQuestionId(attemptId, request.getQuestionId())
                .orElseGet(() -> Answer.builder().attempt(attempt).question(question).build());

        answer.setSelectedOption(request.getSelectedOption().toUpperCase());
        answerRepository.save(answer);
    }

    @Override
    public ResultResponse submitExam(Long attemptId) {
        User student = currentUserProvider.getCurrentUser();

        ExamAttempt attempt = examAttemptRepository.findByIdAndStudentId(attemptId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        if (attempt.getStatus() == AttemptStatus.COMPLETED) {
            throw new ExamAlreadySubmittedException("This exam attempt has already been submitted");
        }

        // PHASE 12: submit is allowed whether time expired or not —
        // grading proceeds either way, using whatever answers were
        // saved before time ran out. This satisfies Section 11's
        // "automatically treat the attempt as submitted" requirement
        // without needing a background scheduler.
        Result result = gradingService.gradeAttempt(attempt);
        return toResultResponse(result, attempt.getExam().getTitle());
    }

    @Override
    public List<ResultResponse> getMyResults() {
        User student = currentUserProvider.getCurrentUser();
        return resultRepository.findByAttempt_Student_Id(student.getId())
                .stream()
                .map(r -> toResultResponse(r, r.getAttempt().getExam().getTitle()))
                .toList();
    }

    @Override
    public ResultResponse getMyResultById(Long resultId) {
        User student = currentUserProvider.getCurrentUser();
        Result result = resultRepository.findByIdAndAttempt_Student_Id(resultId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Result not found with id: " + resultId));
        return toResultResponse(result, result.getAttempt().getExam().getTitle());
    }

    @Override
    public PerformanceResponse getMyPerformance() {
        User student = currentUserProvider.getCurrentUser();
        List<Result> results = resultRepository.findByAttempt_Student_Id(student.getId());

        List<ResultResponse> resultResponses = results.stream()
                .map(r -> toResultResponse(r, r.getAttempt().getExam().getTitle()))
                .toList();

        int totalPassed = (int) results.stream().filter(r -> r.getResultStatus() == ResultStatus.PASS).count();
        int totalFailed = results.size() - totalPassed;

        double averagePercentage = results.stream()
                .mapToDouble(Result::getPercentage)
                .average()
                .orElse(0.0);

        double bestPercentage = results.stream()
                .mapToDouble(Result::getPercentage)
                .max()
                .orElse(0.0);

        return PerformanceResponse.builder()
                .totalExamsAttempted(results.size())
                .totalPassed(totalPassed)
                .totalFailed(totalFailed)
                .averagePercentage(Math.round(averagePercentage * 100.0) / 100.0)
                .bestPercentage(Math.round(bestPercentage * 100.0) / 100.0)
                .results(resultResponses)
                .build();
    }

    // ---- helpers ----

    private Exam findPublishedExamOrThrow(Long examId) {
        return examRepository.findById(examId)
                .filter(exam -> exam.getStatus() == ExamStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found or not available: " + examId));
    }

    private ExamDetailResponse toExamDetailResponse(Exam exam) {
        int questionCount = (int) questionRepository.countByExamId(exam.getId());

        return ExamDetailResponse.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .duration(exam.getDuration())
                .numberOfQuestions(questionCount)
                .totalMarks(exam.getTotalMarks())
                .passingMarks(exam.getPassingMarks())
                .build();
    }

    private QuestionStudentResponse toStudentQuestionResponse(Question question) {
        List<OptionResponse> options = question.getOptions().stream()
                .map(o -> OptionResponse.builder()
                        .id(o.getId())
                        .optionLabel(o.getOptionLabel())
                        .optionText(o.getOptionText())
                        .build())
                .toList();

        return QuestionStudentResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .marks(question.getMarks())
                .options(options)
                .build();
    }

    private ResultResponse toResultResponse(Result result, String examTitle) {
        return ResultResponse.builder()
                .id(result.getId())
                .attemptId(result.getAttempt().getId())
                .examTitle(examTitle)
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