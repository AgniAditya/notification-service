package com.github.agniaditya.notification_service.utils;

public class Constants {
    public static final String APIKEY_CACHE_PREFIX = "apikey:valid:";
    public static final long APIKEY_CACHE_SECONDS = 3600; // cache for 1 hour

    public static final int APIKEY_MAX_REQUESTS = 1000;
    public static final int APIKEY_WINDOW_SECONDS = 3600;

    public static final String IDEMPOTENCY_CACHE_PREFIX = "idempotency:key:";
    public static final long IDEMPOTENCY_CACHE_SECONDS = 86400;

    public static final int IP_MAX_REQUESTS = 100;
    public static final int IP_WINDOW_SECONDS = 60;
}
