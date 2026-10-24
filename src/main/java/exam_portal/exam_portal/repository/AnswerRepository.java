package exam_portal.exam_portal.repository;

import exam_portal.exam_portal.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // All answers saved so far for one attempt — used at grading time.
    List<Answer> findByAttemptId(Long attemptId);

    // Lets us check if the student already answered this question in
    // this attempt, so we UPDATE instead of INSERT a duplicate row.
    Optional<Answer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);
}