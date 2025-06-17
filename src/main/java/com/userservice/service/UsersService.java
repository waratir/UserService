package com.userservice.service;

import com.userservice.dto.UsersDto;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    UsersDto getUserById(UUID id);
    UsersDto getUserByEmail(String email);
    List<UsersDto> getUsersByIds(List<UUID> uuids);
    void createUser(UsersDto userDto);
    void updateUser(UsersDto userDto);
    void deleteUser(UsersDto userDto);
}
