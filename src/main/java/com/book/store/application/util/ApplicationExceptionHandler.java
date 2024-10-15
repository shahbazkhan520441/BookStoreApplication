package com.book.store.application.util;

import com.book.store.application.exception.UserAlreadyExistException;
import com.book.store.application.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private ResponseEntity<ErrorStructure<String>> errorResponse(HttpStatus status, String errorMessage, String rootCause) {
        return ResponseEntity.status(status).body(new ErrorStructure<String>().setStatus(status.value())
                .setMessage(errorMessage).setRootCause(rootCause));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleUserAlreadyExist(UserAlreadyExistException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "Already User is exist");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorStructure<String>> handleUserNotExist(UserNotExistException ex) {
        return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "User not exist");
    }
}