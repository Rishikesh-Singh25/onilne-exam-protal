package exam_portal.exam_portal.repository;

import exam_portal.exam_portal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // "ExamId" here means: walk the "exam" relationship on Question,
    // then filter by its "id" field. Spring Data resolves this
    // automatically from the method name — no query needed.
    List<Question> findByExamId(Long examId);

    long countByExamId(Long examId);
}