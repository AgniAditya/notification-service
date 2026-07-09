package com.github.agniaditya.notification_service.controllers;

import com.github.agniaditya.notification_service.services.GenerateHashKeyService;
import com.github.agniaditya.notification_service.utils.ApiKeyRequest;
import com.github.agniaditya.notification_service.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/api-keys")
public class ApiKeyController {

    @Autowired
    private GenerateHashKeyService generateHashKey;

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

        return new ApiResponse(
                true,
                "api key generated successfully",
                "200",
                generatedKey
        );
    }
}
