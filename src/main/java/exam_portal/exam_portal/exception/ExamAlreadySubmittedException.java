package exam_portal.exam_portal.exception;

public class ExamAlreadySubmittedException extends RuntimeException {
    public ExamAlreadySubmittedException(String message) {
        super(message);
    }
}