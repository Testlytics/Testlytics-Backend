package com.example.Testlytics.DTO;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T responseBody;

    public ApiResponse() {}

    public ApiResponse(String status, String message, T responseBody) {
        this.status = status;
        this.message = message;
        this.responseBody = responseBody;
    }

    // Getters and Setters
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
