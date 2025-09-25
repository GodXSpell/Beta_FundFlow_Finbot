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
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transaction_user", columnList = "user_id"),
        @Index(name = "idx_transaction_bank_account", columnList = "bank_account_id"),
        @Index(name = "idx_transaction_date", columnList = "transaction_date")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the transaction

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User who owns this transaction

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount; // Bank account associated with this transaction

    @Column(nullable = false)
    private BigDecimal amount; // Amount of the transaction

    @Column(nullable = false)
    private String type; // CREDIT or DEBIT

    @Column
    private String category; // Category of the transaction (e.g., groceries, entertainment)

    @Column
    private String description; // Description of the transaction

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate; // Date and time when the transaction occurred

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Timestamp when the transaction record was created

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Timestamp when the transaction record was last updated
}