package com.github.agniaditya.notification_service.services;

import com.github.agniaditya.notification_service.entity.Notification;
import com.github.agniaditya.notification_service.repository.NotificationRepository;
import com.github.agniaditya.notification_service.utils.Constants;
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

    public boolean isExist(String idempotency_key){
        String cachedKey = redisTemplate.opsForValue().get(Constants.IDEMPOTENCY_CACHE_PREFIX + idempotency_key);
        if(cachedKey != null){
            return true;
        }

        Optional<Notification> exist = notificationRepository.findByIdempotencyKey(idempotency_key);
        if(exist.isEmpty()){
            return false;
        }

        redisTemplate.opsForValue().set(
                Constants.IDEMPOTENCY_CACHE_PREFIX + idempotency_key,
                String.valueOf(true),
                Constants.IDEMPOTENCY_CACHE_SECONDS,
                TimeUnit.SECONDS
        );

        return true;
    }
}
