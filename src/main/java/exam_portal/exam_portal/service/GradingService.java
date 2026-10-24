package exam_portal.exam_portal.service;

import exam_portal.exam_portal.entity.ExamAttempt;
import exam_portal.exam_portal.entity.Result;

public interface GradingService {
    /**
     * Grades every question in the attempt's exam against whatever
     * answers the student saved, persists per-answer correctness,
     * saves the Result, and marks the attempt COMPLETED.
     */
    Result gradeAttempt(ExamAttempt attempt);
}