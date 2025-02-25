package dev.folomkin.taskmanager.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
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


    @ExceptionHandler(value = InvalidTaskFieldException.class)
    public ProblemDetail handleInvalidTaskException(InvalidTaskFieldException e, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage());
        problemDetail.setTitle(messageSource.getMessage("errors.400.problemDetailsTitle", new Object[0], null));
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(value = AuthorizeDublicateUserException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ProblemDetail taskNotFound(AuthorizeDublicateUserException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage());
        problemDetail.setTitle(messageSource.getMessage("errors.400.problemDetailsTitle", new Object[0], null));
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }


    @ExceptionHandler(value = ChangeTaskAccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    protected ProblemDetail changeTaskAccessDenied(ChangeTaskAccessDeniedException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        ex.getMessage());
        problemDetail.setTitle(messageSource.getMessage("errors.403.problemDetailsTitle", new Object[0], null));
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ProblemDetail usernameNotFound(UserNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        ex.getMessage());
        problemDetail.setTitle(messageSource.getMessage("errors.403.problemDetailsTitle", new Object[0], null));
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }
}
