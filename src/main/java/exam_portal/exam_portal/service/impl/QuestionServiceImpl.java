package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.dto.OptionResponse;
import exam_portal.exam_portal.dto.QuestionRequest;
import exam_portal.exam_portal.dto.QuestionResponse;
import exam_portal.exam_portal.entity.Exam;
import exam_portal.exam_portal.entity.Option;
import exam_portal.exam_portal.entity.Question;
import exam_portal.exam_portal.exception.InvalidExamException;
import exam_portal.exam_portal.exception.ResourceNotFoundException;
import exam_portal.exam_portal.repository.ExamRepository;
import exam_portal.exam_portal.repository.QuestionRepository;
import exam_portal.exam_portal.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    @Override
    public QuestionResponse addQuestion(Long examId, QuestionRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + examId));

        Question question = Question.builder()
                .questionText(request.getQuestionText())
                .marks(request.getMarks())
                .correctOption(request.getCorrectOption().toUpperCase())
                .exam(exam)
                .build();

        attachOptions(question, request);

        Question saved = questionRepository.save(question);
        recalculateTotalMarks(examId);

        return toResponse(saved);
    }

    @Override
    public List<QuestionResponse> getQuestionsByExam(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with id: " + examId);
        }
        return questionRepository.findByExamId(examId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public QuestionResponse updateQuestion(Long questionId, QuestionRequest request) {
        Question question = findQuestionOrThrow(questionId);

        question.setQuestionText(request.getQuestionText());
        question.setMarks(request.getMarks());
        question.setCorrectOption(request.getCorrectOption().toUpperCase());

        // Mutate the EXISTING collection so Hibernate's orphanRemoval
        // correctly deletes the old option rows — do not reassign
        // question.setOptions(newList) here.
        question.getOptions().clear();
        attachOptions(question, request);

        Question saved = questionRepository.save(question);
        recalculateTotalMarks(question.getExam().getId());

        return toResponse(saved);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        Question question = findQuestionOrThrow(questionId);
        Long examId = question.getExam().getId();

        questionRepository.delete(question);
        recalculateTotalMarks(examId);
    }

    // ---- helpers ----

    /** Builds Option entities from the request's plain string list,
     *  assigning labels A, B, C, D... by position, and validates
     *  that correctOption actually matches one of them. */
    private void attachOptions(Question question, QuestionRequest request) {
        List<String> optionTexts = request.getOptions();
        String correctOption = request.getCorrectOption().toUpperCase();

        boolean correctOptionMatched = false;

        for (int i = 0; i < optionTexts.size(); i++) {
            String label = String.valueOf((char) ('A' + i));

            Option option = Option.builder()
                    .optionLabel(label)
                    .optionText(optionTexts.get(i))
                    .question(question)
                    .build();

            question.getOptions().add(option);

            if (label.equals(correctOption)) {
                correctOptionMatched = true;
            }
        }

        if (!correctOptionMatched) {
            throw new InvalidExamException(
                    "correctOption '" + correctOption + "' does not match any of the " +
                            optionTexts.size() + " provided options (expected one of A-" +
                            (char) ('A' + optionTexts.size() - 1) + ")");
        }
    }

    /** Recomputes Exam.totalMarks as the sum of all its questions' marks. */
    private void recalculateTotalMarks(Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        int total = questions.stream().mapToInt(Question::getMarks).sum();

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id: " + examId));
        exam.setTotalMarks(total);
        examRepository.save(exam);
    }

    private Question findQuestionOrThrow(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + questionId));
    }

    private QuestionResponse toResponse(Question question) {
        List<OptionResponse> options = question.getOptions().stream()
                .map(o -> OptionResponse.builder()
                        .id(o.getId())
                        .optionLabel(o.getOptionLabel())
                        .optionText(o.getOptionText())
                        .build())
                .toList();

        return QuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .marks(question.getMarks())
                .correctOption(question.getCorrectOption())
                .options(options)
                .build();
    }
}