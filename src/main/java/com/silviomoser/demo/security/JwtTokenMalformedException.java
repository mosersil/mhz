package com.silviomoser.demo.security;


import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {

    public JwtTokenMalformedException(String message) {
        super(message);
    }


    public JwtTokenMalformedException(String message, Throwable cause) {
        super(message, cause);
    }
}
