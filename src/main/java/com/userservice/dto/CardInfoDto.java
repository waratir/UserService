package com.userservice.dto;

import com.userservice.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoDto {
    private UUID id;
    private Users user;
    private String holder;
    private LocalDate expirationDate;
    private String number;
}
