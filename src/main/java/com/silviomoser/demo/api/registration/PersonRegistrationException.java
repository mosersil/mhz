package com.silviomoser.demo.api.registration;

public class PersonRegistrationException extends RuntimeException {

    PersonRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
