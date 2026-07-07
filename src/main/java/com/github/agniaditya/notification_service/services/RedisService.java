package com.github.agniaditya.notification_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Set a key with expiry
    public void set(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    // Get a value
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Increment a counter
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    // Check if key exists
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    // Set expiry on existing key
    public void expire(String key, long seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
}
