package com.silviomoser.demo.config;

import com.silviomoser.demo.security.utils.PasswordUtils;
import com.silviomoser.demo.ui.i18.I18Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Created by silvio on 26.08.18.
 */

@Configuration
@PropertySource("${appconf.path:classpath:default.properties}")
public class EnvironmentConfiguration {


    @Autowired
    private Environment environment;

    @Bean
    public ContactConfiguration contactConfiguration() {
        final ContactConfiguration contactConfiguration = new ContactConfiguration();
        contactConfiguration.setContactEmailFrom(environment.getProperty("contact.from"));
        contactConfiguration.setContactEmailTo(environment.getProperty("contact.to"));
        contactConfiguration.setGetContactEmailSubject(environment.getProperty("contact.subject"));
        return contactConfiguration;
    }

    @Bean
    public PaymentConfiguration paymentConfiguration() {
        return new PaymentConfiguration(environment.getProperty("payment.api.privatekey"));
    }

    @Bean
    public PwResetConfiguration pwResetConfiguration() {
        PwResetConfiguration pwResetConfiguration = new PwResetConfiguration();
        pwResetConfiguration.setEmailFrom(environment.getProperty("pwreset.email.from"));
        pwResetConfiguration.setLandingPage(environment.getProperty("pwreset.landingpage"));
        pwResetConfiguration.setBaseUrl(environment.getProperty("pwreset.baseurl", "http://localhost:8085"));
        return pwResetConfiguration;
    }

    @Bean
    public FileServiceConfiguration fileServiceConfiguration() {
        final FileServiceConfiguration fileServiceConfiguration = new FileServiceConfiguration();
        fileServiceConfiguration.setDirectory(environment.getProperty("fileservice.directory", "/tmp"));
        return fileServiceConfiguration;
    }

    @Bean
    public ImageServiceConfiguration imageServiceConfiguration() {
        final ImageServiceConfiguration imageServiceConfiguration = new ImageServiceConfiguration();
        imageServiceConfiguration.setBaseUrl(environment.getProperty("image.baseurl", "http://localhost:8085"));
        imageServiceConfiguration.setStoragePath(environment.getProperty("image.storagePath", "/opt/mhz/images/"));
        imageServiceConfiguration.setAccessKey(environment.getProperty("image.accesskey"));
        imageServiceConfiguration.setEndpoint(environment.getProperty("image.endpoint", "http://127.0.0.1:9000"));
        imageServiceConfiguration.setSecretKey(environment.getProperty("image.secretkey"));
        return imageServiceConfiguration;
    }

    @Bean
    public JwtTokenConfiguration jwtTokenConfiguration() {
        final JwtTokenConfiguration jwtTokenConfiguration = new JwtTokenConfiguration();
        jwtTokenConfiguration.setJwtSecret(environment.getProperty("jwt.secret", PasswordUtils.generateToken(10, false)));
        final String expirationInSecString = environment.getProperty("jwt.expirationInSecs", "3600");
        jwtTokenConfiguration.setJwtExpirationSecs(Integer.parseInt(expirationInSecString));
        return jwtTokenConfiguration;
    }

    @Bean
    @RequestScope
    public I18Helper i18Helper() {
        return new I18Helper();
    }

}
