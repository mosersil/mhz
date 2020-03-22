package com.silviomoser.mhz.services;

import lombok.Getter;

import java.util.ResourceBundle;

@Getter
public class ServiceException extends Exception {

    private static final String RESOURCES = "exception_resources";

    ResourceBundle labels = ResourceBundle.getBundle(RESOURCES);

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }


    public String getLocalizedMessage() {
        if (labels.containsKey(getMessage())) {
            return labels.getString(getMessage());
        }
        return getMessage();
    }


}
