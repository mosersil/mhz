package com.silviomoser.demo.api.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;


    public ApiError(HttpStatus status, String message, @NotNull String error) {
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
