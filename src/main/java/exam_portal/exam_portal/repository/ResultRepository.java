package exam_portal.exam_portal.repository;

import exam_portal.exam_portal.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByAttemptId(Long attemptId);

    List<Result> findByAttempt_Student_Id(Long studentId);

    List<Result> findByAttempt_Exam_Id(Long examId);

    // PHASE 11: enforces that a student can only fetch their OWN
    // result by id — filtering happens in the query itself, not
    // as an after-the-fact check in the service.
    Optional<Result> findByIdAndAttempt_Student_Id(Long id, Long studentId);
}