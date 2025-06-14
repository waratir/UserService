package com.userservice.mapper;

import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {
    CardInfoDto entityToDto(CardInfo entity);

    CardInfo dtoToEntity(CardInfoDto dto);

    void updateCardInfoDto(CardInfoDto cardInfoDto, @MappingTarget CardInfo entity);
}
