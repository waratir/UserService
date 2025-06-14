package com.userservice.exception;

import com.userservice.controller.UsersController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = UsersController.class)
public class UsersErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleUsersNotFound(NotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Users not found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

}
