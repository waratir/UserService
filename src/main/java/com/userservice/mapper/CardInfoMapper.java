package com.userservice.mapper;

import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import com.userservice.entity.User;
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

    default User map(UUID userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
