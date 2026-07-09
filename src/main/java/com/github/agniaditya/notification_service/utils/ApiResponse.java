package com.github.agniaditya.notification_service.utils;

public class ApiResponse {
    private boolean success;
    private String message;
    private String statusCode;
    private String data = null;

    public ApiResponse(boolean success, String message, String statusCode) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ApiResponse(boolean success, String message, String statusCode, String data) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }
}
