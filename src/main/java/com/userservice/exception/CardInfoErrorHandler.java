package com.userservice.exception;

import com.userservice.controller.CardInfoController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice()
public class CardInfoErrorHandler {

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<?> handleCardInfoNotFound(NotFoundException ex) {
//        Map<String, String> body = new HashMap<>();
//        body.put("error", "Empty info not found");
//        body.put("message", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
//    }
}
