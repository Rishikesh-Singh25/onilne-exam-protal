package exam_portal.exam_portal.repository;

import exam_portal.exam_portal.entity.AttemptStatus;
import exam_portal.exam_portal.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    // A student's full attempt history.
    List<ExamAttempt> findByStudentId(Long studentId);

    // Used to enforce "students can only view their own attempt" —
    // combine the attempt id with the logged-in student's id.
    Optional<ExamAttempt> findByIdAndStudentId(Long id, Long studentId);

    // Prevents a student from starting the same exam twice while
    // an attempt is still IN_PROGRESS.
    Optional<ExamAttempt> findByStudentIdAndExamIdAndStatus(
            Long studentId, Long examId, AttemptStatus status);

    // Admin view: all attempts for a given exam.
    List<ExamAttempt> findByExamId(Long examId);
}