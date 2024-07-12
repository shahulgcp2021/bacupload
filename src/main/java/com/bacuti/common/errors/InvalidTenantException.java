package com.bacuti.common.errors;

public class InvalidTenantException extends RuntimeException{

    private final int statusCode;

    public InvalidTenantException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
