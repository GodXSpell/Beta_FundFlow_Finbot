package com.finbot.Beta.repository;

import com.finbot.Beta.entity.BankAccount;
import com.finbot.Beta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findByUser(User user);
    List<BankAccount> findByUserAndIsActiveTrue(User user);
    Optional<BankAccount> findByIdAndUser(UUID id, User user);
    Optional<BankAccount> findByIdAndUserAndIsActiveTrue(UUID id, User user);
}