package com.userservice.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice()
public class UsersErrorHandler {

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<?> handleUsersNotFound(NotFoundException ex) {
//        System.out.println("В хендлере");
//        Map<String, String> body = new HashMap<>();
//        body.put("error", "Users not found");
//        body.put("message", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
//    }

}
