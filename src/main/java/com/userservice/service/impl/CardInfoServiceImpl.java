package com.userservice.service.impl;

import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import com.userservice.entity.User;
import com.userservice.exception.NotFoundException;
import com.userservice.mapper.CardInfoMapper;
import com.userservice.repository.CardInfoRepository;
import com.userservice.repository.UsersRepository;
import com.userservice.service.CardInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardInfoServiceImpl implements CardInfoService {
    private final CacheManager cacheManager;
    private final CardInfoRepository cardInfoRepository;
    private final CardInfoMapper cardInfoMapper;
    private final UsersRepository usersRepository;

    @Override
    @Cacheable(value = "card_info", key = "#id")
    public CardInfoDto getCardInfoById(UUID id) {
        CardInfo cardInfo = cardInfoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card info not found with id: " + id));
        return cardInfoMapper.entityToDto(cardInfo);
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
    @CachePut(value = "card_info", key = "#result.id")
    public void createCardInfo(CardInfoDto cardInfoDto) {
        CardInfo cardInfo = cardInfoMapper.dtoToEntity(cardInfoDto);

        if (cardInfoDto.getUserId() != null) {
            User user = usersRepository.findById(cardInfoDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            cardInfo.setUser(user);
        }

        cardInfoRepository.save(cardInfo);
    }

    @Override
    @CachePut(value = "card_info", key = "#cardInfoDto.id")
    public void updateCardInfo(CardInfoDto cardInfoDto) {
        CardInfo entity = cardInfoRepository.findCardInfoById(cardInfoDto.getId())
                .orElseThrow(() -> new NotFoundException("Card info not found with id: " + cardInfoDto.getId()));
        cardInfoMapper.updateCardInfoDto(cardInfoDto, entity);
        cardInfoRepository.save(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "card_info", key = "#cardInfoDto.id")
    public void deleteCardInfo(CardInfoDto cardInfoDto) {
        CardInfo cardInfo = cardInfoRepository.findCardInfoById(cardInfoDto.getId())
                .orElseThrow(() -> new NotFoundException("Card info not found with id: " + cardInfoDto.getId()));
        cardInfoRepository.delete(cardInfo);
    }
}
