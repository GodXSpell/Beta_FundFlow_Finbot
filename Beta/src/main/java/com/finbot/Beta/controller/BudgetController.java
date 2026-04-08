package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.request.BudgetRequestDto;
import com.finbot.Beta.Dto.response.BudgetResponseDto;
import com.finbot.Beta.security.CustomUserDetails;
import com.finbot.Beta.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BudgetRequestDto request) {

        BudgetResponseDto response = budgetService.createBudget(userDetails.getUser(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDto>> getUserBudgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<BudgetResponseDto> budgets = budgetService.getUserBudgets(userDetails.getUser());
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> getBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID budgetId) {

        BudgetResponseDto budget = budgetService.getBudget(userDetails.getUser(), budgetId);
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> updateBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID budgetId,
            @Valid @RequestBody BudgetRequestDto request) {

        BudgetResponseDto response = budgetService.updateBudget(userDetails.getUser(), budgetId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID budgetId) {

        budgetService.deleteBudget(userDetails.getUser(), budgetId);
        return ResponseEntity.noContent().build();
    }
}
