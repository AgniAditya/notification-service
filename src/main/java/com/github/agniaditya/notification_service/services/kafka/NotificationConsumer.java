package com.github.agniaditya.notification_service.services.kafka;

import com.github.agniaditya.notification_service.utils.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(topics = "notifications.mail", groupId = "email-group")
    public void handleEmail(NotificationEvent event){
        System.out.println("Processing Email Event is: " + event.recipient());
    }

    @KafkaListener(topics = "notifications.sms", groupId = "sms-group")
    public void handleSms(NotificationEvent event){
        System.out.println("Processing SMS Event is: " + event.recipient());
    }

    @KafkaListener(topics = "notifications.push", groupId = "push-group")
    public void handlePush(NotificationEvent event){
        System.out.println("Processing Push Event is: " + event.recipient());
    }
}
