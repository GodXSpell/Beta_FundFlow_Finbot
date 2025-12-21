package com.finbot.Beta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "bank_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the bank account

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // JPA relationship instead of just the ID

    @Column(nullable = false, length = 100)
    private String name; // Name of the bank account

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; // Current balance of the bank account

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType; // Type of the bank account (e.g., savings, checking)

    @Column(name = "bank_name")
    private String bankName; // Name of the bank

    @Column(name = "account_number", length = 50)
    private String accountNumber; // Bank account number

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Timestamp when the account was created

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Timestamp when the account was last updated

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true; // Indicates if the bank account is active or not
}