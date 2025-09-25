package com.finbot.Beta.repository;

import com.finbot.Beta.entity.BankAccount;
import com.finbot.Beta.entity.Transaction;
import com.finbot.Beta.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findByUserOrderByTransactionDateDesc(User user, Pageable pageable);

    List<Transaction> findByUserAndBankAccount(User user, BankAccount bankAccount);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.category = :category ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserAndCategory(
            @Param("user") User user,
            @Param("category") String category);

    @Query("SELECT SUM(CASE WHEN t.type = 'DEBIT' THEN t.amount ELSE 0 END) FROM Transaction t " +
            "WHERE t.user = :user AND t.category = :category AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumExpensesByUserAndCategoryAndDateRange(
            @Param("user") User user,
            @Param("category") String category,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}