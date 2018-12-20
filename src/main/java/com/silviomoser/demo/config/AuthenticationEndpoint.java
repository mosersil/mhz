package com.silviomoser.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationEndpoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/app")) {
            log.debug("Unauthenticated request to admin backend. Redirect to login page");
            response.sendRedirect("/login?returnUrl=%2Fintra");
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
