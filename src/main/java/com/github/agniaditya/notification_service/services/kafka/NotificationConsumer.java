package com.github.agniaditya.notification_service.services.kafka;

import com.github.agniaditya.notification_service.enums.Status;
import com.github.agniaditya.notification_service.repository.NotificationRepository;
import com.github.agniaditya.notification_service.services.EmailService;
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

    @KafkaListener(topics = "notifications.mail", groupId = "email-group")
    public void handleEmail(NotificationEvent event){
        System.out.println("Processing Email Event is: " + event.recipient());
        try{
            emailService.sendEmail(event.recipient(), event.content());

            updateStatus(event.notificationId(), Status.SUCCESS, null);

            System.out.println("Email sent to: " + event.recipient());
        } catch (Exception e) {
            updateStatus(event.notificationId(), Status.FAIL, e.getMessage());

            System.out.println("Email failed: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "notifications.sms", groupId = "sms-group")
    public void handleSms(NotificationEvent event){
        System.out.println("Processing SMS Event is: " + event.recipient());
        System.out.println("SMS sent to: " + event.recipient());
        updateStatus(event.notificationId(), Status.SUCCESS, null);
    }

    @KafkaListener(topics = "notifications.push", groupId = "push-group")
    public void handlePush(NotificationEvent event){
        System.out.println("Processing Push Event is: " + event.recipient());
        System.out.println("PUSH sent to: " + event.recipient());
        updateStatus(event.notificationId(), Status.SUCCESS, null);
    }

    private void updateStatus(String notificationId, Status status, String failureReason){
       notificationRepository.findById(notificationId).ifPresent(notification -> {
           notification.setStatus(status);
           notification.setFailureReason(failureReason);
           if(status == Status.SUCCESS){
               notification.setDeliveredAt(LocalDateTime.now());
           }
           notificationRepository.save(notification);
       });
    }
}
