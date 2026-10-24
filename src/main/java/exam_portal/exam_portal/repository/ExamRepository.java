package exam_portal.exam_portal.repository;

import exam_portal.exam_portal.entity.Exam;
import exam_portal.exam_portal.entity.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    // Students should only ever see PUBLISHED exams.
    List<Exam> findByStatus(ExamStatus status);
}