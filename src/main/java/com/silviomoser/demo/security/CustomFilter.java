package com.silviomoser.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by silvio on 30.07.18.
 */
public class CustomFilter extends UsernamePasswordAuthenticationFilter {



    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        Credentials credentials = null;
        try {
            credentials = mapper.readValue(httpServletRequest.getInputStream(),Credentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        // Allow subclasses to set the "details" property
        setDetails(httpServletRequest, token);

        return this.getAuthenticationManager().authenticate(token);
    }
}
