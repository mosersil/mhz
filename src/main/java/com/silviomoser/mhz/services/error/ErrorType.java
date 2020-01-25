package com.silviomoser.mhz.services.error;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_FOUND(400),
    NOT_AUTHORIZED(403),
    ALREADY_EXIST(400),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500);

    private int httpStatus;

    ErrorType(int httpStatus) {
        this.httpStatus=httpStatus;
    }
}
