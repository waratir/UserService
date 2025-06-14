package com.userservice.mapper;

import com.userservice.dto.UsersDto;
import com.userservice.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersDto entityToDto(Users entity);

    Users dtoToEntity(UsersDto dto);

    void updateUsersFromDto(UsersDto userDto, @MappingTarget Users entity);
}
