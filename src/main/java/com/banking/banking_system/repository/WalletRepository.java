package com.banking.banking_system.repository;

import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository  extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByAccountNumber(String accountNumber);
    Optional<Wallet> findByUserId(Long userId);
    Optional<Wallet> findByUser(User user);

    boolean existsByAccountNumber(String accountNumber);
    List<Wallet> findByIsActiveTrue();
}
