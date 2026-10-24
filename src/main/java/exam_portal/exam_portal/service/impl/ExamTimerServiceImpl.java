package exam_portal.exam_portal.service.impl;

import exam_portal.exam_portal.entity.ExamAttempt;
import exam_portal.exam_portal.service.ExamTimerService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * The single source of truth for "has this attempt run out of time".
 * Both saveAnswer() and submitExam() call this instead of each
 * re-implementing the same date-math, so the rule only lives in
 * one place if it ever needs to change (e.g. adding a grace period).
 */
@Service
public class ExamTimerServiceImpl implements ExamTimerService {

    @Override
    public boolean isTimeExpired(ExamAttempt attempt) {
        LocalDateTime deadline = attempt.getStartTime()
                .plusMinutes(attempt.getExam().getDuration());

        return LocalDateTime.now().isAfter(deadline);
    }
}