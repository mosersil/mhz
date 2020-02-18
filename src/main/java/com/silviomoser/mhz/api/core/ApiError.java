package com.silviomoser.mhz.api.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<ApiErrorDetail> errors;


    public ApiError(HttpStatus status) {
        this.status=status;
        this.message=status.getReasonPhrase();
    }

    public ApiError(HttpStatus status, String message, ApiErrorDetail... error) {
        this.status = status;
        this.message = message;
        if (error!=null) {
            errors = Arrays.asList(error);
        }
    }

}
