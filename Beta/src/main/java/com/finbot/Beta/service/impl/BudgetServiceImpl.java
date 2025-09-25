package com.finbot.Beta.service.impl;

import com.finbot.Beta.Dto.request.BudgetRequestDto;
import com.finbot.Beta.Dto.response.BudgetResponseDto;
import com.finbot.Beta.Exceptions.ResourceNotFoundException;
import com.finbot.Beta.entity.Budget;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.repository.BudgetRepository;
import com.finbot.Beta.service.BudgetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;


    @Override
    @Transactional
    public BudgetResponseDto createBudget(User user, BudgetRequestDto request) {
        Budget budget = Budget.builder()
                .user(user)
                .name(request.getName())
                .category(request.getCategory())
                .amount(request.getAmount())
                .period(request.getPeriod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .currentSpent(BigDecimal.ZERO)
                .isActive(true)
                .build();
        Budget savedBudget = budgetRepository.save(budget);
        return mapToResponse(savedBudget);
    }

    @Override
    public List<BudgetResponseDto> getUserBudgets(User user) {
        return budgetRepository.findByUserAndIsActiveTrue(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BudgetResponseDto getBudget(User user, UUID budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        //verfiy if the budget belongs to the user
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Budget not found");
        }

        return mapToResponse(budget);
    }

    @Override
    @Transactional
    public BudgetResponseDto updateBudget(User user, UUID budgetId, BudgetRequestDto request) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        // Verify if the budget belongs to the user
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Budget not found");
        }

        budget.setName(request.getName());
        budget.setCategory(request.getCategory());
        budget.setAmount(request.getAmount());
        budget.setPeriod(request.getPeriod());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        Budget updatedBudget = budgetRepository.save(budget);
        return mapToResponse(updatedBudget);
    }

    @Override
    @Transactional
    public void deleteBudget(User user, UUID budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        // Verify if the budget belongs to the user
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Budget not found");
        }

        budget.setIsActive(false);
        budgetRepository.save(budget);
    }

    @Override
    public void updateBudgetSpending(User user, String category, BigDecimal amount) {
        budgetRepository.findActiveByUserAndCategory(user, category, LocalDate.now())
                .ifPresent(budget -> {
                    budget.setCurrentSpent(budget.getCurrentSpent().add(amount));
                    budgetRepository.save(budget);
                });
    }

    private BudgetResponseDto mapToResponse(Budget budget) {
        BigDecimal remainingAmount = budget.getAmount().subtract(budget.getCurrentSpent());
        double percentageUsed = budget.getAmount().compareTo(BigDecimal.ZERO) > 0
                ? budget.getCurrentSpent().divide(budget.getAmount(), 4, RoundingMode.HALF_UP).doubleValue() * 100
                : 0.0;

        return BudgetResponseDto.builder()
                .id(budget.getId())
                .userId(budget.getUser().getId())
                .name(budget.getName())
                .category(budget.getCategory())
                .amount(budget.getAmount())
                .period(budget.getPeriod())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .currentSpent(budget.getCurrentSpent())
                .remainingAmount(remainingAmount)
                .percentageUsed(percentageUsed)
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .isActive(budget.getIsActive())
                .build();
    }
}
