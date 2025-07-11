package com.userservice.controller;

import com.userservice.dto.UserDto;
import com.userservice.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = usersService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam @Valid String email) {
        UserDto userDto = usersService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<UserDto>> getUsersByIds(@RequestBody List<UUID> ids) {
        List<UserDto> user = usersService.getUsersByIds(ids);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto userDto) {
        usersService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UserDto userDto) {
        usersService.updateUser(userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        usersService.deleteUser(userDto);
        return ResponseEntity.noContent().build();
    }
}
