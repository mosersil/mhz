package com.silviomoser.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silviomoser.demo.security.AuthenticationResult;
import com.silviomoser.demo.security.JwtAuthenticationFilter;
import com.silviomoser.demo.security.JwtTokenProvider;
import com.silviomoser.demo.security.SecurityUserDetailsService;
import com.silviomoser.demo.security.UsernamePasswordFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by silvio on 10.05.18.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsernamePasswordFilter authenticationFilter() throws Exception {
        UsernamePasswordFilter authenticationFilter = new UsernamePasswordFilter();
        authenticationFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


    private void loginFailureHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationResult result = new AuthenticationResult();
        try {
            result.setMessage(e.getMessage());
            result.setErrorCode(1);
            httpServletResponse.getWriter().write(mapper.writeValueAsString(result));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void loginSuccessHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        log.info("Login Success: " + httpServletRequest);
        ObjectMapper mapper = new ObjectMapper();
        AuthenticationResult result = new AuthenticationResult();
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        try {
            result.setErrorCode(0);
            result.setJwt(jwtToken);
            httpServletResponse.getWriter().write(mapper.writeValueAsString(result));
        } catch (IOException e) {
            log.error("caught error during login: " + e.getMessage(), e);
        }

    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.anonymous().disable().exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEndpoint());

        http
                .cors().and()
                .authorizeRequests()
                .antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll()
                .antMatchers("/app/login").permitAll()
                //.antMatchers("/vaadinServlet/UIDL/**").hasRole("ADMIN")
                //.antMatchers("/app/**").hasRole("ADMIN")
                //.antMatchers("/app").hasRole("ADMIN")
                .antMatchers("/internal/api/**").hasRole("USER")
                .antMatchers("/api/protected/**").hasRole("USER")
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordFilter.class)
                .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordFilter.class);

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");

        http.headers().frameOptions().disable();
        //http.rememberMe().rememberMeServices(rememberMeServices()).key("myAppKey");
        http.sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/")
                .antMatchers("/assets/**")
                .antMatchers("/VAADIN/**")
                .antMatchers("/public/api/**")
                .antMatchers("/index.html");

    }


    @Bean
    public RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("myAppKey", userDetailsService);
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
