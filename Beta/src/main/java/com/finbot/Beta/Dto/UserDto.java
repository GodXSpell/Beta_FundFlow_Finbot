package com.finbot.Beta.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String registeredOn; // Changed to String for simplicity, can be LocalDate if needed
}
