package com.finbot.Beta.service;

import com.finbot.Beta.Dto.request.BudgetRequestDto;
import com.finbot.Beta.Dto.response.BudgetResponseDto;
import com.finbot.Beta.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BudgetService {
    BudgetResponseDto createBudget(User user, BudgetRequestDto request);
    List<BudgetResponseDto> getUserBudgets(User user);
    BudgetResponseDto getBudget(User user, UUID budgetId);
    BudgetResponseDto updateBudget(User user, UUID budgetId, BudgetRequestDto request);
    void deleteBudget(User user, UUID budgetId);
    void updateBudgetSpending(User user, String category, BigDecimal amount);
}
