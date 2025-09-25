package com.finbot.Beta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "budgets", indexes = {
        @Index(name = "idx_budget_user", columnList = "user_id"),
        @Index(name = "idx_budget_category", columnList = "category")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the budget

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User who owns this budget

    @Column(nullable = false)
    private String name; // Name of the budget

    @Column(nullable = false)
    private String category; // Category this budget applies to

    @Column(nullable = false)
    private BigDecimal amount; // Budget limit amount

    @Column(nullable = false)
    private String period; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // Start date of the budget period

    @Column(name = "end_date")
    private LocalDate endDate; // End date of the budget period (optional)

    @Column(name = "current_spent", nullable = false)
    private BigDecimal currentSpent = BigDecimal.ZERO; // Amount spent so far in this budget period

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Timestamp when the budget was created

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Timestamp when the budget was last updated

    @Column(name = "active", nullable = false)
    private Boolean isActive = true; // Indicates if the budget is active or not
}