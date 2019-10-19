package com.silviomoser.mhz.api.core;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private HttpStatus httpStatus;
    private ApiErrorDetail[] errors;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, ApiErrorDetail... errors) {
        super(message);
        this.errors = errors;
    }

    public ApiException(String message, HttpStatus httpStatus, ApiErrorDetail... errors) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
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

    public ApiErrorDetail[] getErrors() {
        return errors;
    }
}
