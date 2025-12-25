package com.finbot.Beta.service.impl;

import com.finbot.Beta.Dto.request.TransactionRequestDto;
import com.finbot.Beta.Dto.response.TransactionResponseDto;
import com.finbot.Beta.Exceptions.InsufficientFundsException;
import com.finbot.Beta.Exceptions.ResourceNotFoundException;
import com.finbot.Beta.entity.BankAccount;
import com.finbot.Beta.entity.Transaction;
import com.finbot.Beta.entity.TransactionType;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.repository.BankAccountRepository;
import com.finbot.Beta.repository.TransactionRepository;
import com.finbot.Beta.service.BankAccountService;
import com.finbot.Beta.service.BudgetService;
import com.finbot.Beta.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;
    private final BudgetService budgetService;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(User user, TransactionRequestDto request) {
        // Validate bank account
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserAndIsActiveTrue(request.getBankAccountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        BigDecimal previousBalance = bankAccount.getBalance();
        BigDecimal newBalance;

        //check for credit debit type and update the balance accordingly
        if(TransactionType.CREDIT == request.getType()){
            newBalance = previousBalance.add(request.getAmount());
        }else if(TransactionType.DEBIT == request.getType()){
            // Check if there are sufficient funds for debit transaction
            if (previousBalance.compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds in account");
            }
            newBalance = previousBalance.subtract(request.getAmount());
        }else{
            throw new IllegalArgumentException("Transaction type must be either CREDIT or DEBIT");
        }

        // Create transaction
        Transaction transaction = Transaction.builder()
                .user(user)
                .bankAccount(bankAccount)
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate() != null ? request.getTransactionDate() : LocalDateTime.now())
                .previousBalance(previousBalance)
                .newBalance(newBalance)
                .build();

        // Save transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Update bank account balance
        bankAccountService.updateBalance(bankAccount.getId(), request.getAmount(), request.getType().name());

        // Update budget if it's a debit transaction with a category
        if (TransactionType.DEBIT == request.getType() && request.getCategory() != null) {
            budgetService.updateBudgetSpending(user, request.getCategory(), request.getAmount());
        }

        // Create response
        return TransactionResponseDto.builder()
                .id(savedTransaction.getId())
                .userId(user.getId())
                .bankAccountId(bankAccount.getId())
                .bankAccountName(bankAccount.getName())
                .amount(savedTransaction.getAmount())
                .type(savedTransaction.getType().name())
                .category(savedTransaction.getCategory())
                .description(savedTransaction.getDescription())
                .transactionDate(savedTransaction.getTransactionDate())
                .previousBalance(previousBalance)
                .newBalance(newBalance)
                .createdAt(savedTransaction.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getUserTransactions(User user, Pageable pageable) {
        return transactionRepository.findByUserOrderByTransactionDateDesc(user, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByBankAccount(User user, UUID bankAccountId) {
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserAndIsActiveTrue(bankAccountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        return transactionRepository.findByUserAndBankAccount(user, bankAccount).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserAndDateRange(user, startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCategory(User user, String category) {
        return transactionRepository.findByUserAndCategory(user, category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransaction(User user, UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        // Verify the transaction belongs to the user
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return mapToResponse(transaction);
    }

    private TransactionResponseDto mapToResponse(Transaction transaction) {
        return TransactionResponseDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .bankAccountId(transaction.getBankAccount().getId())
                .bankAccountName(transaction.getBankAccount().getName())
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .category(transaction.getCategory())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .previousBalance(transaction.getPreviousBalance())
                .newBalance(transaction.getNewBalance())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
