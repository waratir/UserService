package com.userservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {

        super(message);
        System.out.println("In Exception custom");
    }
}
