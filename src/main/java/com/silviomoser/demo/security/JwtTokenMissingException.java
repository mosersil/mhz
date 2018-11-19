package com.silviomoser.demo.security;


import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {

    public JwtTokenMissingException(String message) {
        super(message);
    }


    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
