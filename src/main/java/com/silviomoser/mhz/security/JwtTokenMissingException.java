package com.silviomoser.mhz.security;


import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {

    public JwtTokenMissingException(String message) {
        super(message);
    }


    public JwtTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
