package com.finbot.Beta.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponseDto {
    private UUID id;
    private UUID userId;
    private String name;
    private String category;
    private BigDecimal amount;
    private String period;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal currentSpent;
    private BigDecimal remainingAmount;
    private double percentageUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}