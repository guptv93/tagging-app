package edu.nyu.hml.tagging.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    private static final String MESSAGE_404 = "Try accessing /app/[dataset] with proper x-auth header.";

    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("NoHandlerFoundException: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MESSAGE_404);
    }

}
