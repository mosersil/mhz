package com.silviomoser.mhz.services;

import com.silviomoser.mhz.services.error.ErrorType;
import lombok.Getter;

import java.util.ResourceBundle;

@Getter
public class CrudServiceException extends Exception {
    private ErrorType errorType;

    private static final String RESOURCES = "exception_resources";

    ResourceBundle labels = ResourceBundle.getBundle(RESOURCES);

    public CrudServiceException(ErrorType errorType) {
        super();
        this.errorType=errorType;
    }

    public CrudServiceException(ErrorType errorType, String message) {
        super(message);
        this.errorType=errorType;
    }

    public String getLocalizedMessage() {
        if (labels.containsKey(getMessage())) {
            return labels.getString(getMessage());
        }
        return getMessage();
    }

}
