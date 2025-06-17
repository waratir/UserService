package com.userservice.controller;

import com.userservice.dto.UsersDto;
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
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable UUID id) {
        UsersDto user = usersService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/")
    public ResponseEntity<UsersDto> getUserByEmail(@RequestParam @Valid String email) {
        UsersDto userDto = usersService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<UsersDto>> getUsersByIds(@RequestBody List<UUID> ids) {
        List<UsersDto> user = usersService.getUsersByIds(ids);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UsersDto userDto) {
        usersService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UsersDto userDto) {
        usersService.updateUser(userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        UsersDto userDto = new UsersDto();
        userDto.setId(id);
        usersService.deleteUser(userDto);
        return ResponseEntity.noContent().build();
    }
}
