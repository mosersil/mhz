package com.silviomoser.demo.services;

public class ChangePasswordException extends ServiceException {

    private String context;

    public ChangePasswordException(String context, String message) {
        super(message);
        this.context = context;
    }

    public String getContext() {
        return context;
    }
}
