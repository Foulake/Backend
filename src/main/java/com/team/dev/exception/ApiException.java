package com.team.dev.exception;


import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public ApiException(String message) {
        this.message = message;
    }

    public ApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public ApiException() {
        super();
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
