package com.empik.kulig.app.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
class ApiExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<ApiExceptionResponse> handleNotFound(UserNotExistException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.ofNullable(new ApiExceptionResponse(NOT_FOUND, ex.getMessage()));
    }
}
