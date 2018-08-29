package com.silviomoser.demo.config;

import com.silviomoser.demo.ui.i18.I18Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Created by silvio on 26.08.18.
 */

@Configuration
@PropertySource("file:///${appconf.path:opt}/app.properties")
public class EnvironmentConfiguration {


    @Autowired
    private Environment environment;

    @Bean
    public ContactConfiguration contactConfiguration() {
        final ContactConfiguration contactConfiguration = new ContactConfiguration();
        contactConfiguration.setContactEmailTo(environment.getProperty("contact.to"));
        contactConfiguration.setGetContactEmailSubject(environment.getProperty("contact.subject"));
        return contactConfiguration;
    }

    @Bean
    @RequestScope
    public I18Helper i18Helper() {
        return new I18Helper();
    }

}
