package com.silviomoser.demo.api.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PersonRegistrationErrorAdvice {


    @ResponseBody
    @ExceptionHandler(PersonRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String personRegistrationErrorHandler(PersonRegistrationException ex) {
        return ex.getMessage();
    }
}
