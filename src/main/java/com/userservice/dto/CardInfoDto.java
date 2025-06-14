package com.userservice.dto;

import com.userservice.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Holder number is required")
    @Size(max = 100, message = "Holder should not be more than 100 characters")
    private String holder;
    private LocalDate expirationDate;
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "The card number must be 16 characters long.")
    private String number;
}
