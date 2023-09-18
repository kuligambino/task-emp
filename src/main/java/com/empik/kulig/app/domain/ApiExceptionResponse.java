package com.empik.kulig.app.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
class ApiExceptionResponse {
    private final int status;
    private final String message;

    ApiExceptionResponse(HttpStatus responseCode, String message) {
        this.status = responseCode.value();
        this.message = message;
    }
}
