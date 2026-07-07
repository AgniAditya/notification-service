package com.github.agniaditya.notification_service.services;

import com.github.agniaditya.notification_service.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

@Service
public class ApiKeyValidationService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String CACHE_PREFIX = "apikey:valid:";
    private static final long CACHE_SECONDS = 3600; // cache for 1 hour

    public boolean isValid(String api_key){
        // Step 1 - hash the incoming key
        String hashedKey = hashKey(api_key);

        // Step 2 - Check the Redis cache
        String cached = redisTemplate.opsForValue().get(CACHE_PREFIX + hashedKey);
        if(cached != null){
            return cached.equals("true");
        }

        // Step 3 - not in cache, check database
        boolean isValid = apiKeyRepository
                .findByKeyHash(hashedKey)
                .map(apiKey -> apiKey.getIsActive())
                .orElse(false);

        redisTemplate.opsForValue().set(
                CACHE_PREFIX + hashedKey,
                String.valueOf(isValid),
                CACHE_SECONDS,
                TimeUnit.SECONDS
        );

        return isValid;
    }

    private String hashKey(String rawKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawKey.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}
