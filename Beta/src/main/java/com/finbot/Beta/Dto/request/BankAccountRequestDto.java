package com.finbot.Beta.Dto.request;

import com.finbot.Beta.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String bankName;

    private String accountNumber;

    @PositiveOrZero(message = "Initial balance must be positive or zero")
    private BigDecimal balance = BigDecimal.ZERO;

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}
