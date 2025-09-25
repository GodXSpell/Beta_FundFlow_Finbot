package com.finbot.Beta.Dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequestDto {

    @NotBlank(message = "Budget name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Budget amount is required")
    @Positive(message = "Budget amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Period is required")
    private String period; // DAILY, WEEKLY, MONTHLY, YEARLY

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;
}