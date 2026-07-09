package com.github.agniaditya.notification_service.controllers;

import com.github.agniaditya.notification_service.entity.ApiKey;
import com.github.agniaditya.notification_service.repository.ApiKeyRepository;
import com.github.agniaditya.notification_service.services.GenerateHashKeyService;
import com.github.agniaditya.notification_service.utils.ApiKeyRequest;
import com.github.agniaditya.notification_service.utils.ApiResponse;
import com.github.agniaditya.notification_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/api-keys")
public class ApiKeyController {

    @Autowired
    private GenerateHashKeyService generateHashKey;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @PostMapping("/generate")
    public ApiResponse generateApiKey(@RequestBody ApiKeyRequest data){
        String clientName = data.getClientName().trim();
        String projectName = data.getProjectName().trim();
        if(clientName.isEmpty() || projectName.isEmpty()){
            return new ApiResponse(
                    false,
                    "client and project name is required",
                    "400"
            );
        }

        String generatedKey = generateHashKey.hashKey(clientName,projectName);
        if(generatedKey == null){
            return new ApiResponse(
                    false,
                    "failed to generate api key. TRY AGAIN!",
                    "500"
            );
        }

        boolean isExist = apiKeyRepository.findByKeyHash(generatedKey).isPresent();
        if(isExist){
            return new ApiResponse(
                    false,
                    "Duplicate key found, try to change your client or project name.",
                    "409"
            );
        }

        ApiKey apiKey = new ApiKey();
        apiKey.setClientName(clientName);
        apiKey.setKeyHash(generatedKey);

        apiKeyRepository.save(apiKey);
        System.out.println("api key create successfully...");

        redisTemplate.opsForValue().set(
                Constants.APIKEY_CACHE_PREFIX + generatedKey,
                String.valueOf(true),
                Constants.APIKEY_CACHE_SECONDS,
                TimeUnit.SECONDS
        );

        return new ApiResponse(
                true,
                "api key generated successfully",
                "200",
                generatedKey
        );
    }
}
