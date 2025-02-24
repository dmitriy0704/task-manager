package dev.folomkin.taskmanager.exceptions;

public class ChangeTaskAccessDeniedException extends RuntimeException {
    public ChangeTaskAccessDeniedException(String message) {
        super(message);
    }
}
