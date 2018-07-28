package com.silviomoser.demo.config;

import com.silviomoser.demo.security.SecurityUserDetailsService;
import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

/**
 * Created by silvio on 10.05.18.
 */
@Configuration
@EnableWebSecurity
@EnableVaadin
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@EnableJpaAuditing
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private SecurityUserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
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

        http.
                authorizeRequests()
                .antMatchers("/app/login/**").anonymous()
                .antMatchers("/vaadinServlet/UIDL/**").permitAll()
                .antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll()
                //.antMatchers("/app/**").authenticated();
        .antMatchers("/app/**").hasRole("ADMIN");
                //.anyRequest().authenticated();

        http.httpBasic().disable();
        http.formLogin().disable();

        http.headers().frameOptions().disable();

        //http.logout().addLogoutHandler(new VaadinSessionClosingLogoutHandler()).logoutUrl("/logout")
        //        .logoutSuccessUrl("/login?logout").permitAll();

        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/app/login"));

        http.rememberMe().rememberMeServices(rememberMeServices()).key("myAppKey");

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

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("myAppKey", userDetailsService);
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new SessionFixationProtectionStrategy();
    }

}
