package com.github.agniaditya.notification_service.services;

import com.github.agniaditya.notification_service.entity.Notifications;
import com.github.agniaditya.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class IdempotencyKeyService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String CACHE_PREFIX = "idempotency:key:";
    private static final long CACHE_SECONDS = 86400;

    public boolean isExist(String idempotency_key){
        String cachedKey = redisTemplate.opsForValue().get(CACHE_PREFIX + idempotency_key);
        if(cachedKey != null){
            return true;
        }

        Optional<Notifications> exist = notificationRepository.findByIdempotencyKey(idempotency_key);
        if(exist.isEmpty()){
            return false;
        }

        redisTemplate.opsForValue().set(
                CACHE_PREFIX + idempotency_key,
                String.valueOf(true),
                CACHE_SECONDS,
                TimeUnit.SECONDS
        );

        return true;
    }
}
