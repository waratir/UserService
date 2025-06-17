package com.userservice.service.impl;

import com.userservice.dto.UsersDto;
import com.userservice.entity.Users;
import com.userservice.exception.NotFoundException;
import com.userservice.mapper.UsersMapper;
import com.userservice.repository.UsersRepository;
import com.userservice.service.UsersService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final CacheManager cacheManager;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Override
    @Cacheable(value = "users", key = "#id")
    public UsersDto getUserById(UUID id) {
        Users users = usersRepository.findUsersById(id)
                .orElseThrow(() -> new NotFoundException("User not found with Id: " + id));
        return usersMapper.entityToDto(users);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UsersDto getUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .map(usersMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UsersDto> getUsersByIds(List<UUID> uuids) {
        List<Users> users = usersRepository.findByIdIn(uuids);
        if (users.isEmpty()) {
            throw new NotFoundException("Users not found for provided ids");
        }
        return users.stream()
                .map(usersMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createUser(UsersDto userDto) {
        Users entity = usersMapper.dtoToEntity(userDto);
        Users createdEntity = usersRepository.save(entity);

        cacheManager.getCache("users").put(createdEntity.getId(), usersMapper.entityToDto(createdEntity));
    }

    @Override
    @Transactional
    public void updateUser(UsersDto userDto) {
        Users entity = usersRepository.findUsersById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("User not found with Id: " + userDto.getId()));
        usersMapper.updateUsersFromDto(userDto, entity);
        Users updatedEntity = usersRepository.save(entity);

        cacheManager.getCache("users").put(updatedEntity.getId(), usersMapper.entityToDto(updatedEntity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userDto.id")
    public void deleteUser(UsersDto userDto) {
        Users entity = usersRepository.findUsersById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("User not found with Id: " + userDto.getId()));
        usersRepository.delete(entity);
    }
}
