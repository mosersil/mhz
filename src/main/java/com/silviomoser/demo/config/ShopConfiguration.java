package com.silviomoser.demo.config;

import com.paymill.context.PaymillContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopConfiguration {

    @Bean
    public PaymillContext paymillContext() {
        return new PaymillContext( "" );
    }
}
