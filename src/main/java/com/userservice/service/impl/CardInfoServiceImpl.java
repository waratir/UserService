package com.userservice.service.impl;

import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import com.userservice.exception.NotFoundException;
import com.userservice.mapper.CardInfoMapper;
import com.userservice.repository.CardInfoRepository;
import com.userservice.service.CardInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardInfoServiceImpl implements CardInfoService {
    private final CardInfoRepository cardInfoRepository;
    private final CardInfoMapper cardInfoMapper;

    @Override
    public CardInfoDto getCardInfoById(UUID id) {
        return cardInfoRepository.findById(id)
                .map(cardInfoMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException("Card info not found with id: " + id));
    }

    @Override
    public List<CardInfoDto> getCardInfosByIds(List<UUID> uuids) {
        List<CardInfo> cardInfos = cardInfoRepository.findByIdIn(uuids);
        if (cardInfos.isEmpty()) {
            throw new NotFoundException("Card info not found for provided ids");
        }
        return cardInfos.stream()
                .map(cardInfoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createCardInfo(CardInfoDto cardInfoDto) {
        CardInfo cardInfo = cardInfoMapper.dtoToEntity(cardInfoDto);
        cardInfoRepository.save(cardInfo);
    }

    @Override
    public void updateCardInfo(CardInfoDto cardInfoDto) {
        CardInfo cardInfo = cardInfoRepository.findCardInfoById(cardInfoDto.getId())
                .orElseThrow(() -> new NotFoundException("Card info not found with id: " + cardInfoDto.getId()));
        cardInfoMapper.updateCardInfoDto(cardInfoDto, cardInfo);
    }

    @Override
    @Transactional
    public void deleteCardInfo(CardInfoDto cardInfoDto) {
        CardInfo cardInfo = cardInfoRepository.findCardInfoById(cardInfoDto.getId())
                .orElseThrow(() -> new NotFoundException("Card info not found with id: " + cardInfoDto.getId()));
        cardInfoRepository.delete(cardInfo);
    }
}
