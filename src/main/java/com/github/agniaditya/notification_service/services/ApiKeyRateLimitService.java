package com.github.agniaditya.notification_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ApiKeyRateLimitService {
    private static final int MAX_REQUESTS = 1000;
    private static final int WINDOW_SECONDS = 3600;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String api_key){
        String key = "apikey:" + api_key;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            redisTemplate.expire(key, WINDOW_SECONDS, TimeUnit.SECONDS);
        }

        return count <= MAX_REQUESTS;
    }
}
