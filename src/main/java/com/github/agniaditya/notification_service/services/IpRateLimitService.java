package com.github.agniaditya.notification_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IpRateLimitService {
    private static final int MAX_REQUESTS = 100;
    private static final int WINDOW_SECONDS = 60;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String ip){
        String key = "ip:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            redisTemplate.expire(key, WINDOW_SECONDS, TimeUnit.SECONDS);
        }

        return count <= MAX_REQUESTS;
    }
}
