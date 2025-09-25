package com.finbot.Beta.service.impl;

import com.finbot.Beta.Dto.request.BankAccountRequestDto;
import com.finbot.Beta.Dto.response.BankAccountResponseDto;
import com.finbot.Beta.Exceptions.ResourceNotFoundException;
import com.finbot.Beta.entity.BankAccount;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.repository.BankAccountRepository;
import com.finbot.Beta.service.BankAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Override
    @Transactional
    public BankAccountResponseDto createBankAccount(User user, BankAccountRequestDto request) {
        BankAccount bankAccount = BankAccount.builder()
                .user(user)
                .name(request.getName())
                .balance(request.getBalance())
                .accountType(request.getAccountType())
                .bankName(request.getBankName())
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
                .accountType(bankAccount.getAccountType())
                .bankName(bankAccount.getBankName())
                .createdAt(bankAccount.getCreatedAt())
                .updatedAt(bankAccount.getUpdatedAt())
                .isActive(bankAccount.getIsActive())
                .build();
    }
}
