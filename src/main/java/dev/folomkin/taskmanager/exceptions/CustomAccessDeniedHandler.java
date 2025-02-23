package dev.folomkin.taskmanager.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Date;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOGGER
            = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {

//        Authentication auth
//                = SecurityContextHolder.getContext().getAuthentication();
//
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(403);
            response.getWriter().write(
                    new ErrorMessage(
                            HttpStatus.FORBIDDEN.value(),
                            new Date(),
                            exc.getMessage(),
                            request.getRequestURI()).getMessage()
            );
    }
}