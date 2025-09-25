package com.finbot.Beta.service;

import com.finbot.Beta.Dto.request.TransactionRequestDto;
import com.finbot.Beta.Dto.response.TransactionResponseDto;
import com.finbot.Beta.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponseDto createTransaction(User user, TransactionRequestDto request);
    Page<TransactionResponseDto> getUserTransactions(User user, Pageable pageable);
    List<TransactionResponseDto> getTransactionsByBankAccount(User user, UUID bankAccountId);
    List<TransactionResponseDto> getTransactionsByDateRange(User user, LocalDateTime startDate, LocalDateTime endDate);
    List<TransactionResponseDto> getTransactionsByCategory(User user, String category);
    TransactionResponseDto getTransaction(User user, UUID transactionId);
}
