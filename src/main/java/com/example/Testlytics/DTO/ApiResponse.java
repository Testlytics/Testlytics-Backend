package com.example.Testlytics.DTO;

import java.util.List;

public class ApiResponse<T> {
    private int statusCode;
    private String status;
    private String message;
    private T responseBody;

    // Default Constructor
    public ApiResponse() {}

    // Parameterized Constructor
    public ApiResponse(int statusCode, String status, String message, T responseBody) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.responseBody = responseBody;
    }

    // Constructor for List<UserDTO> response
    public ApiResponse(String status, String message, List<UserDTO> userDTOs) {
        this.statusCode = 200; // Default success code
        this.status = status;
        this.message = message;
        this.responseBody = (T) userDTOs;
    }

    // Constructor for image response
    public ApiResponse(String status, String message, String imageBase64) {
        this.statusCode = 200; // Default success code
        this.status = status;
        this.message = message;
        this.responseBody = (T) imageBase64;
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
