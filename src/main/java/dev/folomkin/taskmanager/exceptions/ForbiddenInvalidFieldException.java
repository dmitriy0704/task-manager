package dev.folomkin.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenInvalidFieldException extends RuntimeException {
    public ForbiddenInvalidFieldException(String message) {
        super(message);
    }
}
