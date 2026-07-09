package com.github.agniaditya.notification_service.controllers;

import com.github.agniaditya.notification_service.services.*;
import com.github.agniaditya.notification_service.utils.ApiKeyRequest;
import com.github.agniaditya.notification_service.utils.ApiResponse;
import com.github.agniaditya.notification_service.utils.NotificationRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private IpRateLimitService ipBasedRateLimit;

    @Autowired
    private ApiKeyValidationService apiKeyValidation;

    @Autowired
    private IdempotencyKeyService idempotencyKeyService;

    @Autowired
    private ApiKeyRateLimitService apiKeyRateLimitService;

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
        String api_key = req.getHeader("notify-api-key");
        if(api_key == null || api_key.isEmpty()){
            return new ApiResponse(
                    false,
                    "API key not found",
                    "404");
        }
        if(!apiKeyValidation.isValid(api_key)){
            return new ApiResponse(
                    false,
                    "Not a valid API key",
                    "401");
        }

        // Step 3 - Idempotency Check
        String idempotency_key = req.getHeader("notify-idempotency-key");
        if(idempotency_key == null || idempotency_key.isEmpty()){
            return new ApiResponse(
                    false,
                    "Idempotency key not found",
                    "404");
        }
        if(idempotencyKeyService.isExist(idempotency_key)){
            return new ApiResponse(
                    false,
                    "duplicate request, Idempotency key should be unique for each request",
                    "409");
        }

        // Step 4 - API key based rate limiting
        if(!apiKeyRateLimitService.isAllowed(api_key)){
            return new ApiResponse(
                    false,
                    "Too many requests",
                    "429");
        }

        System.out.println(data.getContent());
        System.out.println(data.getChannel());
        System.out.println(data.getRecipient());

        return new ApiResponse(
                true,
                "notification send successfully",
                "200");
    }
}