package com.userservice.mapper;

import com.userservice.dto.UserDto;
import com.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsersMapper {

    UserDto entityToDto(User entity);

    User dtoToEntity(UserDto dto);

    void updateUsersFromDto(UserDto userDto, @MappingTarget User entity);
}
