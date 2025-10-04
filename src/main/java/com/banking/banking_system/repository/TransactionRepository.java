package com.banking.banking_system.repository;

import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
public interface TransactionRepository extends JpaRepository <Transaction,Long>{
    @Query(value = "SELECT * FROM Transaction t WHERE t.from_wallet_id = :walletId or t.to_wallet_id = :walletId ORDER BY t.timestamp DESC",nativeQuery = true)
    List<Transaction> findTransactionsByWallet (@Param("walletId")Long walletId);
    @Query(value = "SELECT * FROM Transaction t WHERE (t.from_wallet_id = :walletId or t.to_wallet_id = :walletId)AND t.timestamp BETWEEN :startDate AND :endDate ORDER BY t.timestamp DESC",nativeQuery = true)
    List<Transaction> findTransactionsbyWalletAndDateRange(@Param("walletId")Long walletId,
                                                           @Param("startDate")LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

}
