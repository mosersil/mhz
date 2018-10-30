package com.silviomoser.demo.security;

import lombok.Data;
import lombok.ToString;

/**
 * Created by silvio on 31.07.18.
 */
@Data
@ToString
public class AuthenticationResult {
    private int errorCode;
    private String message;
}
