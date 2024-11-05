package com.example.demo.exceptions;

public class ApiException {
    private String message;

    private ApiException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ApiException create(Throwable e) {
        return new ApiException(e.getMessage());
    }

    public static ApiException create(String message) {
        return new ApiException(message);
    }
}