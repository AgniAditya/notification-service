package com.github.agniaditya.notification_service.services;

import com.github.agniaditya.notification_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IpRateLimitService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String ip){
        String key = "ip:rate:limit:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            redisTemplate.expire(key, Constants.IP_WINDOW_SECONDS, TimeUnit.SECONDS);
        }

        return count <= Constants.IP_MAX_REQUESTS;
    }
}
