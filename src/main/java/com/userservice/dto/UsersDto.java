package com.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class UsersDto {
    private UUID id;
    @NotNull
    @Size(max = 50, message = "Name should not be more than 50 characters")
    private String name;
    @NotNull
    @Size(max = 50, message = "Name should not be more than 50 characters")
    private String surname;
    private LocalDate birthday;
    @Email
    private String email;
}
