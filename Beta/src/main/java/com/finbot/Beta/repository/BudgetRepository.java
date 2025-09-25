package com.finbot.Beta.repository;

import com.finbot.Beta.entity.Budget;
import com.finbot.Beta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findByUserAndIsActiveTrue(User user);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.category = :category " +
            "AND b.isActive = true AND :currentDate BETWEEN b.startDate AND COALESCE(b.endDate, :currentDate)")
    Optional<Budget> findActiveByUserAndCategory(
            @Param("user") User user,
            @Param("category") String category,
            @Param("currentDate") LocalDate currentDate);

    @Query("SELECT b FROM Budget b WHERE b.user = :user AND b.isActive = true " +
            "AND :currentDate BETWEEN b.startDate AND COALESCE(b.endDate, :currentDate)")
    List<Budget> findAllActiveByUser(
            @Param("user") User user,
            @Param("currentDate") LocalDate currentDate);
}