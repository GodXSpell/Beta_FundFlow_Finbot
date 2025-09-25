package com.finbot.Beta.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {
    private UUID id;
    private UUID userId;
    private UUID bankAccountId;
    private String bankAccountName;
    private BigDecimal amount;
    private String type;
    private String category;
    private String description;
    private LocalDateTime transactionDate;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    private LocalDateTime createdAt;
}
