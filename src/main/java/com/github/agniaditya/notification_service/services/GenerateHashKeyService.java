package com.github.agniaditya.notification_service.services;


import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class GenerateHashKeyService {

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

            return new_api_key;
        } catch (Exception e) {
            return null;
        }
    }
}