package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.request.TransactionRequestDto;
import com.finbot.Beta.Dto.response.TransactionResponseDto;
import com.finbot.Beta.security.CustomUserDetails;
import com.finbot.Beta.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TransactionRequestDto request) {

        TransactionResponseDto response = transactionService.createTransaction(userDetails.getUser(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getUserTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20, sort = "transactionDate,desc") Pageable pageable) {

        Page<TransactionResponseDto> transactions = transactionService.getUserTransactions(userDetails.getUser(), pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID accountId) {

        List<TransactionResponseDto> transactions = transactionService.getTransactionsByBankAccount(userDetails.getUser(), accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/daterange")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByDateRange(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<TransactionResponseDto> transactions = transactionService.getTransactionsByDateRange(userDetails.getUser(), startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String category) {

        List<TransactionResponseDto> transactions = transactionService.getTransactionsByCategory(userDetails.getUser(), category);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransaction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID transactionId) {

        TransactionResponseDto transaction = transactionService.getTransaction(userDetails.getUser(), transactionId);
        return ResponseEntity.ok(transaction);
    }
}

