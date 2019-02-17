package com.silviomoser.demo.config;

import com.silviomoser.demo.services.EmailSenderService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public EmailSenderService emailSenderService() {
        return Mockito.mock(EmailSenderService.class);
    }
}

