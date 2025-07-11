package com.userservice.mapper;

import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import com.userservice.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {
    @Mapping(source = "user.id", target = "userId")
    CardInfoDto entityToDto(CardInfo entity);

    @Mapping(target = "user", source = "userId")
    CardInfo dtoToEntity(CardInfoDto dto);

    void updateCardInfoDto(CardInfoDto cardInfoDto, @MappingTarget CardInfo entity);

    default Users map(UUID userId) {
        if (userId == null) {
            return null;
        }
        Users user = new Users();
        user.setId(userId);
        return user;
    }
}
