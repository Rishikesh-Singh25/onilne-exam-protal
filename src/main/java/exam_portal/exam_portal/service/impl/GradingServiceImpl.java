package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.entity.*;
import exam_portal.exam_portal.repository.AnswerRepository;
import exam_portal.exam_portal.repository.ExamAttemptRepository;
import exam_portal.exam_portal.repository.QuestionRepository;
import exam_portal.exam_portal.repository.ResultRepository;
import exam_portal.exam_portal.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GradingServiceImpl implements GradingService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ResultRepository resultRepository;
    private final ExamAttemptRepository examAttemptRepository;

    /**
     * @Transactional matters here: this method writes to Answer,
     * Result AND ExamAttempt. If anything fails partway through
     * (e.g. saving the Result), we want ALL of it rolled back
     * rather than leaving the attempt half-graded.
     */
    @Override
    @Transactional
    public Result gradeAttempt(ExamAttempt attempt) {
        Exam exam = attempt.getExam();

        List<Question> questions = questionRepository.findByExamId(exam.getId());
        List<Answer> existingAnswers = answerRepository.findByAttemptId(attempt.getId());

        // Map answers by questionId so each question can be looked up
        // in O(1) instead of scanning the list per question.
        Map<Long, Answer> answersByQuestionId = existingAnswers.stream()
                .collect(java.util.stream.Collectors.toMap(a -> a.getQuestion().getId(), Function.identity()));

        int correctCount = 0;
        int marksObtained = 0;

        for (Question question : questions) {
            Answer answer = answersByQuestionId.get(question.getId());

            boolean isCorrect = answer != null
                    && question.getCorrectOption().equalsIgnoreCase(answer.getSelectedOption());

            int marksForThisQuestion = isCorrect ? question.getMarks() : 0;

            if (answer != null) {
                // Only persist grading fields for questions the student
                // actually attempted — unanswered ones simply have no row.
                answer.setIsCorrect(isCorrect);
                answer.setMarksObtained(marksForThisQuestion);
                answerRepository.save(answer);
            }

            if (isCorrect) {
                correctCount++;
                marksObtained += marksForThisQuestion;
            }
        }

        int totalQuestions = questions.size();
        int wrongCount = totalQuestions - correctCount;
        int totalMarks = exam.getTotalMarks() != null ? exam.getTotalMarks() : 0;

        double percentage = totalMarks == 0 ? 0.0 : (marksObtained * 100.0) / totalMarks;

        ResultStatus resultStatus = marksObtained >= exam.getPassingMarks()
                ? ResultStatus.PASS
                : ResultStatus.FAIL;

        Result result = Result.builder()
                .attempt(attempt)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctCount)
                .wrongAnswers(wrongCount)
                .totalMarks(totalMarks)
                .marksObtained(marksObtained)
                .percentage(Math.round(percentage * 100.0) / 100.0) // 2 decimal places
                .resultStatus(resultStatus)
                .build();

        Result savedResult = resultRepository.save(result);

        attempt.setScore(marksObtained);
        attempt.setSubmitTime(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.COMPLETED);
        examAttemptRepository.save(attempt);

        return savedResult;
    }
}