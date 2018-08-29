package com.silviomoser.demo.data;

/**
 * Created by silvio on 20.08.18.
 */
public class EmailStatus {
    private boolean success;
    private String errorDetails;

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
