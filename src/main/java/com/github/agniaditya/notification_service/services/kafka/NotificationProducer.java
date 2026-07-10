package com.github.agniaditya.notification_service.services.kafka;

import com.github.agniaditya.notification_service.enums.Channels;
import com.github.agniaditya.notification_service.utils.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void sendToMail(NotificationEvent event){
        kafkaTemplate.send("notifications.mail", event);
    }

    public void sendToSms(NotificationEvent event){
        kafkaTemplate.send("notifications.sms", event);
    }

    public void sendToPush(NotificationEvent event){
        kafkaTemplate.send("notifications.push", event);
    }

    public void route(NotificationEvent event){
        switch (event.channel()){
            case Channels.GMAIL -> sendToMail(event);
            case Channels.SMS -> sendToSms(event);
            case Channels.PUSH -> sendToPush(event);
            default -> throw new RuntimeException("Unknown channel: " + event.channel());
        }
    }
}
