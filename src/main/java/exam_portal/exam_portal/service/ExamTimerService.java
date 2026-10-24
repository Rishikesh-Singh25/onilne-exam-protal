package exam_portal.exam_portal.service;

import exam_portal.exam_portal.entity.ExamAttempt;

public interface ExamTimerService {
    /** True if the attempt's allowed duration has elapsed since startTime. */
    boolean isTimeExpired(ExamAttempt attempt);
}