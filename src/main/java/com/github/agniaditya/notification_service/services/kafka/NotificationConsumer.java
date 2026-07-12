package com.github.agniaditya.notification_service.services.kafka;

import com.github.agniaditya.notification_service.enums.Status;
import com.github.agniaditya.notification_service.repository.NotificationRepository;
import com.github.agniaditya.notification_service.services.EmailService;
import com.github.agniaditya.notification_service.services.RetryService;
import com.github.agniaditya.notification_service.utils.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationConsumer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RetryService retryService;

    @KafkaListener(topics = "notifications.mail", groupId = "email-group")
    public void handleEmail(NotificationEvent event){
        System.out.println("Processing Email Event is: " + event.recipient());
        try{
            emailService.sendEmail(event.recipient(), event.content());

            markSuccess(event.notificationId());

            System.out.println("Email sent to: " + event.recipient());
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
            retryService.handleFailure(event, e.getMessage());
        }
    }

    @KafkaListener(topics = "notifications.sms", groupId = "sms-group")
    public void handleSms(NotificationEvent event){
        System.out.println("Processing SMS Event is: " + event.recipient());
        System.out.println("SMS sent to: " + event.recipient());
        markSuccess(event.notificationId());
    }

    @KafkaListener(topics = "notifications.push", groupId = "push-group")
    public void handlePush(NotificationEvent event){
        System.out.println("Processing Push Event is: " + event.recipient());
        System.out.println("PUSH sent to: " + event.recipient());
        markSuccess(event.notificationId());
    }

    @KafkaListener(topics = "notifications.dead", groupId = "dead-group")
    public void handleDead(NotificationEvent event){
        System.out.println("DEAD LETTER: " + event.notificationId() + "Failed for: "+ event.recipient());
    }

    private void markSuccess(String notificationId){
       notificationRepository.findById(notificationId).ifPresent(notification -> {
           notification.setStatus(Status.SUCCESS);
           notification.setDeliveredAt(LocalDateTime.now());
           notificationRepository.save(notification);
       });
    }
}
