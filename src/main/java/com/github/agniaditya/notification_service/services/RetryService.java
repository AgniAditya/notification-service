package com.github.agniaditya.notification_service.services;

import com.github.agniaditya.notification_service.enums.Status;
import com.github.agniaditya.notification_service.repository.NotificationRepository;
import com.github.agniaditya.notification_service.services.kafka.NotificationProducer;
import com.github.agniaditya.notification_service.utils.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    private static final int MAX_RETRY = 3;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private NotificationRepository notificationRepository;

    public void handleFailure(NotificationEvent event, String failureReason){

        if(event.retryCount() < MAX_RETRY){

            // delay count calculated as 2^retryCount
            int delay = (int) Math.pow(2, event.retryCount());

            System.out.println("Retrying notification: " + event.notificationId()
                    + " attempt: " + (event.retryCount() + 1)
                    + " after " + delay + " seconds");

            try{
                Thread.sleep(delay * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            NotificationEvent newEvent = new NotificationEvent(
                    event.notificationId(),
                    event.content(),
                    event.recipient(),
                    event.channel(),
                    event.retryCount() + 1
            );
            notificationProducer.route(newEvent);

            updateRetryCount(newEvent.notificationId(), newEvent.retryCount());
        }
        else{
            System.out.println("Max retries exhausted for: "
                    + event.notificationId() + " moving to DLQ");

            // Send to DLQ
            notificationProducer.snedToDead(event);

            updateFailed(event.notificationId(), failureReason);
        }
    }

    private void updateRetryCount(String id, int retryCount){
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRetryCount(retryCount);
            notificationRepository.save(notification);
        });
    }

    private void updateFailed(String id, String failureReason){
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setStatus(Status.FAIL);
            notification.setFailureReason(failureReason);
            notificationRepository.save(notification);
        });
    }
}
