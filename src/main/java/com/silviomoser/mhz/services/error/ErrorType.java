package com.silviomoser.mhz.services.error;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_FOUND(404, "Not found"),
    NOT_AUTHORIZED(403, "Not authorized"),
    ALREADY_EXIST(400, "Already exists"),
    BAD_REQUEST(400, "Bad request"),
    INTERNAL_SERVER_ERROR(500, "Internal server error");

    private int httpStatus;
    private String message;

    ErrorType(int httpStatus, String message) {
        this.httpStatus=httpStatus;
        this.message=message;
    }
}
