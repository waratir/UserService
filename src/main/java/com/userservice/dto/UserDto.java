package com.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    @Size(max = 50, message = "Name should not be more than 50 characters")
    private String name;
    @Size(max = 50, message = "Name should not be more than 50 characters")
    private String surname;
    private LocalDate birthDate;
    @Email
    private String email;
}
