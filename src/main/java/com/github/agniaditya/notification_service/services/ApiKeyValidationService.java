package com.github.agniaditya.notification_service.services;

import com.github.agniaditya.notification_service.repository.ApiKeyRepository;
import com.github.agniaditya.notification_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ApiKeyValidationService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public boolean isValid(String api_key){
        // Step 1 - Check the Redis cache
        String cached = redisTemplate.opsForValue().get(Constants.APIKEY_CACHE_PREFIX + api_key);
        if(cached != null){
            return cached.equals("true");
        }

        // Step 2 - not in cache, check database
        boolean isValid = apiKeyRepository
                .findByKeyHash(api_key)
                .map(apiKey -> apiKey.getIsActive())
                .orElse(false);

        // Step 3 - cache api key in redis if valid
        redisTemplate.opsForValue().set(
                Constants.APIKEY_CACHE_PREFIX + api_key,
                String.valueOf(isValid),
                Constants.APIKEY_CACHE_SECONDS,
                TimeUnit.SECONDS
        );

        return isValid;
    }
}
