package edu.nyu.hml.tagging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * AppExceptionHandler
 * This class is a place to handle exceptions thrown by various controllers.
 *
 * Method annotated ExceptionHander(NoHandlerFoundException) doesn't get called
 * because Spring isn't configured to throw NoHandlerFoundException exception.
 */
@RestControllerAdvice
public class AppExceptionHandler {

    private static final String MESSAGE_404 = "Try accessing /app/[dataset] with proper x-auth header.";

    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<?> handleException(NoHandlerFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MESSAGE_404);
    }

}
