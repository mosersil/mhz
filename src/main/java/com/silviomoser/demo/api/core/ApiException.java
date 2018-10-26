package com.silviomoser.demo.api.core;

public class ApiException extends RuntimeException {

    ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
