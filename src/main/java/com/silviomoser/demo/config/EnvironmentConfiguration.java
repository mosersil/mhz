package com.silviomoser.demo.config;

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
        pwResetConfiguration.setEmailFrom(environment.getProperty(""));
        pwResetConfiguration.setLandingPage("");
        return pwResetConfiguration;
    }

    @Bean
    public FileServiceConfiguration fileServiceConfiguration() {
        final FileServiceConfiguration fileServiceConfiguration = new FileServiceConfiguration();
        fileServiceConfiguration.setDirectory(environment.getProperty("fileservice.directory", "/tmp"));
        return fileServiceConfiguration;
    }

    @Bean
    @RequestScope
    public I18Helper i18Helper() {
        return new I18Helper();
    }

}
