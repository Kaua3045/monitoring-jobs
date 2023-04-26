package com.kaua.monitoring.jobs.configurations;

import com.kaua.monitoring.jobs.services.SQSService;
import com.kaua.monitoring.jobs.services.gateways.MessengerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfiguration {

    @Bean
    public MessengerGateway messengerGateway() {
        return new SQSService();
    }
}
