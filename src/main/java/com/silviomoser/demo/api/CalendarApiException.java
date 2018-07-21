package com.silviomoser.demo.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by silvio on 24.05.18.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CalendarApiException extends RuntimeException {

    public CalendarApiException(String message, Throwable t) {
        super(message, t);
    }
}
