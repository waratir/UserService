package com.userservice.controller;

import com.userservice.dto.CardInfoDto;
import com.userservice.service.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/card_infos")
@RequiredArgsConstructor
public class CardInfoController {
    private final CardInfoService cardInfoService;

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoDto> getCardInfoById(@PathVariable UUID id) {
        CardInfoDto cardInfoDto = cardInfoService.getCardInfoById(id);
        return ResponseEntity.ok(cardInfoDto);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<CardInfoDto>> getCardInfosByIds(@RequestBody List<UUID> ids) {
        List<CardInfoDto> cardInfoDto = cardInfoService.getCardInfosByIds(ids);
        return ResponseEntity.ok(cardInfoDto);
    }

    @PostMapping
    public ResponseEntity<Void> createCardInfo(@RequestBody @Valid CardInfoDto cardInfoDto) {
        cardInfoService.createCardInfo(cardInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateCardInfo(@RequestBody @Valid CardInfoDto cardInfoDto) {
        cardInfoService.updateCardInfo(cardInfoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardInfo(@PathVariable UUID id) {
        CardInfoDto cardInfoDto = new CardInfoDto();
        cardInfoDto.setId(id);
        cardInfoService.deleteCardInfo(cardInfoDto);
        return ResponseEntity.noContent().build();
    }
}
