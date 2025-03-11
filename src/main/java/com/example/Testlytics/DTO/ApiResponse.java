package com.example.Testlytics.DTO;

public class ApiResponse<T> {
    private int statusCode;
    private String status;
    private String message;
    private T responseBody;

    public ApiResponse() {}

    public ApiResponse(int statusCode, String status, String message, T responseBody) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.responseBody = responseBody;
    }

    // Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}