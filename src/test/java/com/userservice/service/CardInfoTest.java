package com.userservice.service;

import com.userservice.dto.CardInfoDto;
import com.userservice.entity.CardInfo;
import com.userservice.exception.NotFoundException;
import com.userservice.mapper.CardInfoMapper;
import com.userservice.repository.CardInfoRepository;
import com.userservice.service.impl.CardInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardInfoTest {

    @InjectMocks
    private CardInfoServiceImpl cardInfoService;

    @Mock
    private CardInfoRepository cardInfoRepository;

    @Mock
    private CardInfoMapper cardInfoMapper;

    @Mock
    private Cache cache;

    @Mock
    private CacheManager cacheManager;

    private UUID cardInfoId;
    private CardInfoDto cardInfoDto;
    private CardInfo cardInfoEntity;

    @BeforeEach
    public void setup() {
        lenient().when(cacheManager.getCache("card_info")).thenReturn(cache);

        cardInfoId = UUID.randomUUID();
        cardInfoEntity = new CardInfo();
        cardInfoEntity.setId(cardInfoId);
        cardInfoEntity.setHolder("Test Holder");
        cardInfoEntity.setNumber("1234567812345678");
        cardInfoEntity.setExpirationDate(LocalDate.of(2025, 12, 31));

        cardInfoDto = new CardInfoDto();
        cardInfoDto.setId(cardInfoId);
        cardInfoDto.setHolder("Test Holder");
        cardInfoDto.setNumber("1234567812345678");
        cardInfoDto.setExpirationDate(LocalDate.of(2025, 12, 31));
    }

    @Test
    public void testGetCardInfoById_Found() {
        when(cardInfoRepository.findById(cardInfoId)).thenReturn(Optional.of(cardInfoEntity));
        when(cardInfoMapper.entityToDto(cardInfoEntity)).thenReturn(cardInfoDto);

        CardInfoDto result = cardInfoService.getCardInfoById(cardInfoId);

        assertEquals(cardInfoDto, result);
        verify(cardInfoRepository).findById(cardInfoId);
        verify(cardInfoMapper).entityToDto(cardInfoEntity);
    }

    @Test
    public void testGetCardInfoById_NotFound() {
        when(cardInfoRepository.findById(cardInfoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardInfoService.getCardInfoById(cardInfoId));
        verify(cardInfoRepository).findById(cardInfoId);
    }

    @Test
    public void testGetCardInfosByIds() {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<CardInfo> entities = Arrays.asList(
                new CardInfo(),
                new CardInfo(),
                new CardInfo()
        );
        List<CardInfoDto> cardInfoDtos = Arrays.asList(
                new CardInfoDto(),
                new CardInfoDto(),
                new CardInfoDto()
        );

        when(cardInfoRepository.findByIdIn(ids)).thenReturn(entities);

        when(cardInfoMapper.entityToDto(entities.get(0))).thenReturn(cardInfoDtos.get(0));
        when(cardInfoMapper.entityToDto(entities.get(1))).thenReturn(cardInfoDtos.get(1));
        when(cardInfoMapper.entityToDto(entities.get(2))).thenReturn(cardInfoDtos.get(2));

        List<CardInfoDto> result = cardInfoService.getCardInfosByIds(ids);

        assertEquals(3, result.size());

        assertEquals(cardInfoDtos.get(0), result.get(0));
        assertEquals(cardInfoDtos.get(1), result.get(1));
        assertEquals(cardInfoDtos.get(2), result.get(2));

        verify(cardInfoMapper).entityToDto(entities.get(0));
        verify(cardInfoMapper).entityToDto(entities.get(1));
        verify(cardInfoMapper).entityToDto(entities.get(2));
    }

    @Test
    public void testGetCardInfosByIds_NotFound() {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        when(cardInfoRepository.findByIdIn(ids)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> cardInfoService.getCardInfosByIds(ids));
    }

    @Test
    public void testCreateCardInfo() {
        when(cardInfoMapper.dtoToEntity(any(CardInfoDto.class))).thenReturn(cardInfoEntity);
        when(cardInfoRepository.save(any(CardInfo.class))).thenReturn(cardInfoEntity);
        when(cardInfoMapper.entityToDto(any(CardInfo.class))).thenReturn(cardInfoDto);

        cardInfoService.createCardInfo(cardInfoDto);

        verify(cardInfoMapper).dtoToEntity(cardInfoDto);
        verify(cardInfoRepository).save(any(CardInfo.class));
        verify(cache).put(eq(cardInfoEntity.getId()), eq(cardInfoDto));
    }

    @Test
    public void testUpdateCardInfo() {
        when(cardInfoRepository.findCardInfoById(any())).thenReturn(Optional.of(cardInfoEntity));
        doNothing().when(cardInfoMapper).updateCardInfoDto(any(), any());
        when(cardInfoRepository.save(any(CardInfo.class))).thenReturn(cardInfoEntity);
        when(cardInfoMapper.entityToDto(any(CardInfo.class))).thenReturn(cardInfoDto);

        cardInfoService.updateCardInfo(cardInfoDto);

        verify(cardInfoRepository).findCardInfoById(cardInfoId);
        verify(cardInfoMapper).updateCardInfoDto(eq(cardInfoDto), eq(cardInfoEntity));
        verify(cardInfoRepository).save(eq(cardInfoEntity));
        verify(cache).put(eq(cardInfoEntity.getId()), eq(cardInfoDto));
    }

    @Test
    public void testUpdateCardInfo_NotFound() {
        when(cardInfoRepository.findCardInfoById(cardInfoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardInfoService.updateCardInfo(cardInfoDto));
    }

    @Test
    public void testDeleteCardInfo() {
        when(cardInfoRepository.findCardInfoById(any())).thenReturn(Optional.of(cardInfoEntity));

        cardInfoService.deleteCardInfo(cardInfoDto);

        verify(cardInfoRepository).delete(cardInfoEntity);
    }

    @Test
    public void testDeleteCardInfo_NotFound() {
        when(cardInfoRepository.findCardInfoById(cardInfoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cardInfoService.deleteCardInfo(cardInfoDto));
    }
}
