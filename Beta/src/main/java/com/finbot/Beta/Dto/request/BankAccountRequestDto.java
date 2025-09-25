package com.finbot.Beta.Dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountRequestDto {
    @NotBlank(message = "Account name is required")
    private String name;

    @PositiveOrZero(message = "Initial balance must be positive or zero")
    private BigDecimal balance = BigDecimal.ZERO;

    @NotBlank(message = "Account type is required")
    private String accountType;

    private String bankName;
}
