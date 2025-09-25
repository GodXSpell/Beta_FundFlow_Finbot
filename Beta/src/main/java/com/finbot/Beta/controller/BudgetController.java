package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.request.BudgetRequestDto;
import com.finbot.Beta.Dto.response.BudgetResponseDto;
import com.finbot.Beta.entity.User;
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
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BudgetRequestDto request) {

        BudgetResponseDto response = budgetService.createBudget(user, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDto>> getUserBudgets(
            @AuthenticationPrincipal User user) {

        List<BudgetResponseDto> budgets = budgetService.getUserBudgets(user);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> getBudget(
            @AuthenticationPrincipal User user,
            @PathVariable UUID budgetId) {

        BudgetResponseDto budget = budgetService.getBudget(user, budgetId);
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> updateBudget(
            @AuthenticationPrincipal User user,
            @PathVariable UUID budgetId,
            @Valid @RequestBody BudgetRequestDto request) {

        BudgetResponseDto response = budgetService.updateBudget(user, budgetId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(
            @AuthenticationPrincipal User user,
            @PathVariable UUID budgetId) {

        budgetService.deleteBudget(user, budgetId);
        return ResponseEntity.noContent().build();
    }
}
