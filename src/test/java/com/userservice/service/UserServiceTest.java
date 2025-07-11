package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.entity.User;
import com.userservice.exception.NotFoundException;
import com.userservice.mapper.UsersMapper;
import com.userservice.repository.UsersRepository;
import com.userservice.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collections;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final String USER_EMAIL = "test@test.com";

    @InjectMocks
    private UsersServiceImpl usersService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UsersMapper usersMapper;

    @Mock
    private Cache cache;

    @Mock
    private CacheManager cacheManager;

    private UUID userId;
    private UserDto userDto;
    private User userEntity;

    @BeforeEach
    public void setup() {
        lenient().when(cacheManager.getCache("users")).thenReturn(cache);
        userId = UUID.randomUUID();

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("TestName");
        userDto.setSurname("TestSurname");
        userDto.setEmail(USER_EMAIL);

        userEntity = new User();
        userEntity.setId(userId);
        userEntity.setName("TestName");
        userEntity.setSurname("TestSurname");
        userEntity.setEmail(USER_EMAIL);
    }

    @Test
    public void testGetUserById() {
        when(usersRepository.findUsersById(userId)).thenReturn(Optional.of(userEntity));
        when(usersMapper.entityToDto(userEntity)).thenReturn(userDto);

        UserDto result = usersService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userDto, result);
        verify(usersRepository).findUsersById(userId);
        verify(usersMapper).entityToDto(userEntity);
    }

    @Test
    public void testGetUserById_NotFound() {
        UUID userId = UUID.randomUUID();

        when(usersRepository.findUsersById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> usersService.getUserById(userId));
    }

    @Test
    public void testGetUserByEmail() {
        when(usersRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
        when(usersMapper.entityToDto(userEntity)).thenReturn(userDto);

        UserDto result = usersService.getUserByEmail(USER_EMAIL);

        assertEquals(userDto, result);
        assertNotNull(result);
        verify(usersRepository).findByEmail(USER_EMAIL);
        verify(usersMapper).entityToDto(userEntity);
    }

    @Test
    public void testGetUserByEmail_NotFound() {
        when(usersRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> usersService.getUserByEmail(any()));
    }

    @Test
    public void testGetUsersByIds_Found() {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<User> entities = Arrays.asList(
                new User(),
                new User(),
                new User()
        );
        List<UserDto> userDtos = Arrays.asList(
                new UserDto(),
                new UserDto(),
                new UserDto()
        );

        when(usersRepository.findByIdIn(ids)).thenReturn(entities);

        when(usersMapper.entityToDto(entities.get(0))).thenReturn(userDtos.get(0));
        when(usersMapper.entityToDto(entities.get(1))).thenReturn(userDtos.get(1));
        when(usersMapper.entityToDto(entities.get(2))).thenReturn(userDtos.get(2));

        List<UserDto> result = usersService.getUsersByIds(ids);

        assertEquals(3, result.size());

        assertEquals(userDtos.get(0), result.get(0));
        assertEquals(userDtos.get(1), result.get(1));
        assertEquals(userDtos.get(2), result.get(2));

        verify(usersMapper).entityToDto(entities.get(0));
        verify(usersMapper).entityToDto(entities.get(1));
        verify(usersMapper).entityToDto(entities.get(2));
    }

    @Test
    public void testGetUsersByIds_NotFound() {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        when(usersRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> usersService.getUsersByIds(ids));
    }

    @Test
    public void testCreateUser() {
        when(usersMapper.dtoToEntity(any(UserDto.class))).thenReturn(userEntity);
        when(usersRepository.save(any(User.class))).thenReturn(userEntity);
        when(usersMapper.entityToDto(any(User.class))).thenReturn(userDto);

        usersService.createUser(userDto);

        verify(usersMapper).dtoToEntity(userDto);
        verify(usersRepository).save(any(User.class));
        verify(cache).put(eq(userEntity.getId()), eq(userDto));
    }

    @Test
    public void testUpdateUser() {
        when(usersRepository.findUsersById(any())).thenReturn(Optional.of(userEntity));
        doNothing().when(usersMapper).updateUsersFromDto(any(), any());
        when(usersRepository.save(any(User.class))).thenReturn(userEntity);
        when(usersMapper.entityToDto(any(User.class))).thenReturn(userDto);

        usersService.updateUser(userDto);

        verify(usersRepository).findUsersById(userId);
        verify(usersMapper).updateUsersFromDto(eq(userDto), eq(userEntity));
        verify(usersRepository).save(eq(userEntity));
        verify(cache).put(eq(userEntity.getId()), eq(userDto));
    }

    @Test
    public void testUpdateUser_NotFound() {
        when(usersRepository.findUsersById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> usersService.updateUser(userDto));
    }

    @Test
    public void testDeleteUser_Success() {
        when(usersRepository.findUsersById(userId)).thenReturn(Optional.of(userEntity));

        usersService.deleteUser(userDto);

        verify(usersRepository).findUsersById(userId);
        verify(usersRepository).delete(eq(userEntity));
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(usersRepository.findUsersById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> usersService.deleteUser(userDto));
    }

}
