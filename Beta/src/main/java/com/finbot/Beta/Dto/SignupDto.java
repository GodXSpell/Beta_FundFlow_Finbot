package com.finbot.Beta.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SignupDto {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    //Optional field for registration date
    private String registeredOn; // Changed to String for simplicity, can be LocalDate if needed
}
