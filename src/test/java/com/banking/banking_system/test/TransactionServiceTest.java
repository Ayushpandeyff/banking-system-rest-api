package com.banking.banking_system.test;

import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.enums.TransactionType;
import com.banking.banking_system.repository.TransactionRepository;
import com.banking.banking_system.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;
private Wallet toWallet;
private Wallet fromWallet;
private Transaction transaction;

@BeforeEach
    void setup(){
    toWallet=new Wallet();
    toWallet.setId(1L);
    toWallet.setBalance(new BigDecimal(100));
    fromWallet=new Wallet();
    fromWallet.setId(2L);
    fromWallet.setBalance(new BigDecimal(100));

    transaction=new Transaction();
    transaction.setId(1L);
}
@Test
    void shouldCreateTransaction(){
    BigDecimal amount=new BigDecimal(100);
    when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
     Transaction result=transactionService.createTranscation(toWallet,fromWallet,amount, TransactionType.TRANSFER,"test");
     assertNotNull(result);
    verify(transactionRepository, times(1)).save(any(Transaction.class));

}
}
