package com.finbot.Beta.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.finbot.Beta.Dto.request.BankAccountRequestDto;
import com.finbot.Beta.Dto.response.BankAccountResponseDto;
import com.finbot.Beta.Exceptions.ResourceNotFoundException;
import com.finbot.Beta.entity.BankAccount;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.repository.BankAccountRepository;
import com.finbot.Beta.repository.UserRepository;
import com.finbot.Beta.service.BankAccountService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BankAccountResponseDto createBankAccount(UUID userId, BankAccountRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Check for duplicate account name
        if (bankAccountRepository.existsByNameAndUserAndIsActiveTrue(request.getName(), user)) {
            throw new IllegalArgumentException("Account with name '" + request.getName() + "' already exists");
        }
        
        // Check for duplicate account number (if provided)
        if (request.getAccountNumber() != null && !request.getAccountNumber().isEmpty()) {
            if (bankAccountRepository.existsByAccountNumberAndUserAndIsActiveTrue(request.getAccountNumber(), user)) {
                throw new IllegalArgumentException("Account with number '" + request.getAccountNumber() + "' already exists");
            }
        }
        
        BankAccount bankAccount = BankAccount.builder()
                .user(user)
                .name(request.getName())
                .balance(request.getBalance())
                .accountType(request.getAccountType())
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .isActive(true)
                .build();

        BankAccount savedAccount = bankAccountRepository.save(bankAccount);
        return mapToResponse(savedAccount);
    }

    @Override
    public List<BankAccountResponseDto> getUserBankAccounts(User user) {
        return bankAccountRepository.findByUserAndIsActiveTrue(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountResponseDto getBankAccount(User user, UUID accountId) {
        BankAccount account = bankAccountRepository.findByIdAndUserAndIsActiveTrue(accountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));
        return mapToResponse(account);
    }

    @Override
    @Transactional
    public BankAccountResponseDto updateBankAccount(User user, UUID accountId, BankAccountRequestDto request) {
        BankAccount account = bankAccountRepository.findByIdAndUserAndIsActiveTrue(accountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        account.setName(request.getName());
        account.setAccountType(request.getAccountType());
        account.setBankName(request.getBankName());

        BankAccount updatedAccount = bankAccountRepository.save(account);
        return mapToResponse(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteBankAccount(User user, UUID accountId) {
        BankAccount account = bankAccountRepository.findByIdAndUserAndIsActiveTrue(accountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        account.setIsActive(false);
        bankAccountRepository.save(account);
    }

    @Override
    @Transactional
    public void updateBalance(UUID accountId, BigDecimal amount, String transactionType) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        BigDecimal newBalance;
        if ("CREDIT".equals(transactionType)) {
            newBalance = account.getBalance().add(amount);
        } else if ("DEBIT".equals(transactionType)) {
            newBalance = account.getBalance().subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Insufficient funds in account");
            }
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        account.setBalance(newBalance);
        bankAccountRepository.save(account);
    }

    private BankAccountResponseDto mapToResponse(BankAccount bankAccount) {
        return BankAccountResponseDto.builder()
                .id(bankAccount.getId())
                .userId(bankAccount.getUser().getId())
                .name(bankAccount.getName())
                .balance(bankAccount.getBalance())
                .accountType(bankAccount.getAccountType().name())
                .bankName(bankAccount.getBankName())
                .createdAt(bankAccount.getCreatedAt())
                .updatedAt(bankAccount.getUpdatedAt())
                .isActive(bankAccount.getIsActive())
                .build();
    }
}
