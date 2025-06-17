package com.userservice.repository;

import com.userservice.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardInfoRepository extends JpaRepository<CardInfo, UUID> {

    Optional<CardInfo> findCardInfoById(UUID id);

    List<CardInfo> findByIdIn(Collection<UUID> ids);

    @Query(value = "SELECT * FROM card_info WHERE user_id IN :userIds", nativeQuery = true)
    List<CardInfo> findCardsByIds(@Param("userIds") List<UUID> userIds);
}
