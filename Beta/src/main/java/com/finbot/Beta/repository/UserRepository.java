package com.finbot.Beta.repository;

import com.finbot.Beta.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

      Optional<User> findByEmailAndPassword(String email, String password); //Method will come handy when changing password

      Optional<User> findByEmail(String mail);
}
