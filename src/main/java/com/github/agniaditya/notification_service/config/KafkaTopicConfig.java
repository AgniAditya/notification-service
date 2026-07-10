package com.github.agniaditya.notification_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic mailTopic(){
        return new NewTopic("notifications.mail", 1, (short) 1);
    }

    @Bean
    public NewTopic smsTopic(){
        return new NewTopic("notifications.mail", 1, (short) 1);
    }

    @Bean
    public NewTopic pushTopic(){
        return new NewTopic("notifications.push", 1, (short) 1);
    }
}
