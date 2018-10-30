package com.silviomoser.demo.api.core;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private HttpStatus httpStatus;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
