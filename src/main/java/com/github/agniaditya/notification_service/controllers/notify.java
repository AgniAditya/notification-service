package com.github.agniaditya.notification_service.controllers;

import com.github.agniaditya.notification_service.utils.MessageRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api_v1")
public class notify {
    @PostMapping("/notify")
    public boolean sendNotification(HttpServletRequest req,
                                    HttpServletResponse res,
                                    @RequestBody MessageRequest data){
        String ip = req.getRemoteAddr();
        String api_key = req.getHeader("notify-api-key");
        String idempotency_key = req.getHeader("notify-idempotency-key");

        System.out.println("IP address: " + ip);
        System.out.println("API Key: " + api_key);
        System.out.println("Idempotency Key: " + idempotency_key);
        System.out.println("Content: " + data.getContent());
        System.out.println("Channel: " + data.getChannel());
        System.out.println("Recipient: " + data.getRecipient());

        return true;
    }
}