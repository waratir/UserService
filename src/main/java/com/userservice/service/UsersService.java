package com.userservice.service;

import com.userservice.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    UserDto getUserById(UUID id);
    UserDto getUserByEmail(String email);
    List<UserDto> getUsersByIds(List<UUID> uuids);
    void createUser(UserDto userDto);
    void updateUser(UserDto userDto);
    void deleteUser(UserDto userDto);
}
