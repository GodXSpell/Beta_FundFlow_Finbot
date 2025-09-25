package com.finbot.Beta.service;

import com.finbot.Beta.Dto.request.BankAccountRequestDto;
import com.finbot.Beta.Dto.response.BankAccountResponseDto;
import com.finbot.Beta.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BankAccountService {
    BankAccountResponseDto createBankAccount(User user, BankAccountRequestDto request);
    List<BankAccountResponseDto> getUserBankAccounts(User user);
    BankAccountResponseDto getBankAccount(User user, UUID accountId);
    BankAccountResponseDto updateBankAccount(User user, UUID accountId, BankAccountRequestDto request);
    void deleteBankAccount(User user, UUID accountId);
    void updateBalance(UUID accountId, BigDecimal amount, String transactionType);
}

