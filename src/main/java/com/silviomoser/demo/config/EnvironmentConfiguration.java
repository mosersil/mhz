package com.silviomoser.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

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

}
