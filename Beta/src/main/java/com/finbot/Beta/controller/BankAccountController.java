package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.request.BankAccountRequestDto;
import com.finbot.Beta.Dto.response.BankAccountResponseDto;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<BankAccountResponseDto> createBankAccount(
            @RequestParam UUID userId,
            @Valid @RequestBody BankAccountRequestDto request) {

        BankAccountResponseDto response = bankAccountService.createBankAccount(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/userBankAccounts")
    public ResponseEntity<List<BankAccountResponseDto>> getUserBankAccounts(
            @AuthenticationPrincipal User user) {

        List<BankAccountResponseDto> accounts = bankAccountService.getUserBankAccounts(user);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountResponseDto> getBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable UUID accountId) {

        BankAccountResponseDto account = bankAccountService.getBankAccount(user, accountId);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<BankAccountResponseDto> updateBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable UUID accountId,
            @Valid @RequestBody BankAccountRequestDto request) {

        BankAccountResponseDto response = bankAccountService.updateBankAccount(user, accountId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable UUID accountId) {

        bankAccountService.deleteBankAccount(user, accountId);
        return ResponseEntity.noContent().build();
    }
}
