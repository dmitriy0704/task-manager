package dev.folomkin.taskmanager.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public GlobalControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler(value = NoSuchElementException.class)
    public ProblemDetail handleInvalidInputException(NoSuchElementException e, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        e.getMessage());
        problemDetail.setTitle(messageSource.getMessage("errors.404.problemDetailsTitle", new Object[0], null));
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }


    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorMessage> usernameNotFound(UserNotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(
                        HttpStatus.UNAUTHORIZED.value(),
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(value = ChangeTaskAccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    protected ResponseEntity<ErrorMessage> changeTaskAccessDenied(ChangeTaskAccessDeniedException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(
                        HttpStatus.FORBIDDEN.value(),
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler(value = AuthorizeDublicateUserException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorMessage> taskNotFound(AuthorizeDublicateUserException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(
                        HttpStatus.UNAUTHORIZED.value(),
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)));
    }

}
