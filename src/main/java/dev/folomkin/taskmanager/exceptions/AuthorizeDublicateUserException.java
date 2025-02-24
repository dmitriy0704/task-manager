package dev.folomkin.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizeDublicateUserException extends RuntimeException {
    public AuthorizeDublicateUserException(String message) {
        super(message);
    }
}
