package com.github.agniaditya.notification_service.services;


import com.github.agniaditya.notification_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

@Service
public class GenerateHashKeyService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String APIKEY_PREFIX = "notify:apikey:";

    public String hashKey(String client_name, String project_name){
        String key = APIKEY_PREFIX + project_name + ":" + client_name;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            String new_api_key = hex.toString();

            redisTemplate.opsForValue().set(
                    Constants.APIKEY_CACHE_PREFIX + new_api_key,
                    String.valueOf(true),
                    Constants.APIKEY_CACHE_SECONDS,
                    TimeUnit.SECONDS
            );

            return new_api_key;
        } catch (Exception e) {
            return null;
        }
    }
}