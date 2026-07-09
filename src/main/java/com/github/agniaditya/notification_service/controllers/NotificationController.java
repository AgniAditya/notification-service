package com.github.agniaditya.notification_service.controllers;

import com.github.agniaditya.notification_service.entity.ApiKey;
import com.github.agniaditya.notification_service.entity.Notification;
import com.github.agniaditya.notification_service.repository.ApiKeyRepository;
import com.github.agniaditya.notification_service.repository.NotificationRepository;
import com.github.agniaditya.notification_service.services.*;
import com.github.agniaditya.notification_service.utils.ApiResponse;
import com.github.agniaditya.notification_service.utils.NotificationRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private IpRateLimitService ipBasedRateLimit;

    @Autowired
    private ApiKeyValidationService apiKeyValidation;

    @Autowired
    private IdempotencyKeyService idempotencyKeyService;

    @Autowired
    private ApiKeyRateLimitService apiKeyRateLimitService;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping
    public ApiResponse sendNotification(HttpServletRequest req,
                                    @RequestBody NotificationRequest data){
        // 1. Ip based rate limiting.
        String ip = req.getRemoteAddr();
        if(ip.isEmpty()){
            return new ApiResponse(
                    false,
                    "IP address not found",
                    "404");
        }
        if(!ipBasedRateLimit.isAllowed(ip)){
            return new ApiResponse(
                    false,
                    "Too many requests",
                    "429");
        }

        // 2. Validate API Key.
        String apiKey = req.getHeader("notify-api-key");
        if(apiKey == null || apiKey.isEmpty()){
            return new ApiResponse(
                    false,
                    "API key not found",
                    "404");
        }
        if(!apiKeyValidation.isValid(apiKey)){
            return new ApiResponse(
                    false,
                    "Not a valid API key",
                    "401");
        }

        // Step 3 - Idempotency Check
        String idempotencyKey = req.getHeader("notify-idempotency-key");
        if(idempotencyKey == null || idempotencyKey.isEmpty()){
            return new ApiResponse(
                    false,
                    "Idempotency key not found",
                    "404");
        }
        if(idempotencyKeyService.isExist(idempotencyKey)){
            return new ApiResponse(
                    false,
                    "duplicate request, Idempotency key should be unique for each request",
                    "409");
        }

        // Step 4 - API key based rate limiting
        if(!apiKeyRateLimitService.isAllowed(apiKey)){
            return new ApiResponse(
                    false,
                    "Too many requests",
                    "429");
        }

        // Step 5 - Write Notification to DB
        ApiKey client = apiKeyRepository
                .findByKeyHash(apiKey)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Notification notification = new Notification();
        notification.setClientId(client);
        notification.setRecipient(data.getRecipient());
        notification.setChannel(data.getChannel());
        notification.setContent(data.getContent());
        notification.setIdempotencyKey(idempotencyKey);

        notificationRepository.save(notification);
        System.out.println("Notification created...");

        // Step 6 - Return HTTP 202 ACCEPTED
        return new ApiResponse(
                true,
                "notification send successfully",
                "200");
    }
}