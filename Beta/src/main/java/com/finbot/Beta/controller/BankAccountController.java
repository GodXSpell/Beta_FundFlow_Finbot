package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.request.BankAccountRequestDto;
import com.finbot.Beta.Dto.response.BankAccountResponseDto;
import com.finbot.Beta.security.CustomUserDetails;
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

    @PostMapping("/create")
    public ResponseEntity<BankAccountResponseDto> createBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BankAccountRequestDto request) {

        BankAccountResponseDto response = bankAccountService.createBankAccount(userDetails.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BankAccountResponseDto>> getUserBankAccounts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<BankAccountResponseDto> accounts = bankAccountService.getUserBankAccounts(userDetails.getUser());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/get/{accountId}")
    public ResponseEntity<BankAccountResponseDto> getBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID accountId) {

        BankAccountResponseDto account = bankAccountService.getBankAccount(userDetails.getUser(), accountId);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<BankAccountResponseDto> updateBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID accountId,
            @Valid @RequestBody BankAccountRequestDto request) {

        BankAccountResponseDto response = bankAccountService.updateBankAccount(userDetails.getUser(), accountId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID accountId) {

        bankAccountService.deleteBankAccount(userDetails.getUser(), accountId);
        return ResponseEntity.noContent().build();
    }
}
