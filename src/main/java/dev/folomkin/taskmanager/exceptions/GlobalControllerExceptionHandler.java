package dev.folomkin.taskmanager.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public GlobalControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler(value = TaskNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorMessage> taskNotFound(TaskNotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(
                        HttpStatus.NOT_FOUND.value(),
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)));
    }



//    @ExceptionHandler(value = RuntimeException.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    protected ResponseEntity<ErrorMessage> handleInternalServerError(RuntimeException ex, WebRequest request) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorMessage(
//                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                        new Date(),
//                        new Exception("Ошибка сервера").getMessage(),
//                        request.getDescription(false)));
//    }


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                "Доступ запрещен", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

}
