package com.receiptorc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// This class provides personalized exceptions to user.
@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {
    /**
     * Will make all generic exceptions be formated
     * @param e a reference of Exception
     * @return JSON with message error and Http Code
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> genericErrorHandler(Exception e) {
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> notFoundErrorHandler(NotFoundException e) {
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(TechnicalException.class)
    public final ResponseEntity<Object> technicalException(TechnicalException e){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }
}
