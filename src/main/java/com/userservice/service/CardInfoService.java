package com.userservice.service;

import com.userservice.dto.CardInfoDto;

import java.util.List;
import java.util.UUID;

public interface CardInfoService {
    CardInfoDto getCardInfoById (UUID id);
    List<CardInfoDto> getCardInfosByIds (List<UUID> uuids);
    void createCardInfo (CardInfoDto cardInfoDto);
    void updateCardInfo (CardInfoDto cardInfoDto);
    void deleteCardInfo (CardInfoDto cardInfoDto);
}
