package com.banking.banking_system.test;

import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.enums.TransactionType;
import com.banking.banking_system.enums.TranscationStatus;
import com.banking.banking_system.repository.TransactionRepository;
import com.banking.banking_system.repository.UserRepository;
import com.banking.banking_system.repository.WalletRepository;
import com.banking.banking_system.service.MoneyTransferService;
import com.banking.banking_system.service.TransactionService;
import com.banking.banking_system.service.UserSrevice;
import com.banking.banking_system.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.AssertionErrors;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MoneyTransferServiceTest {
    @Mock
    private UserSrevice userSrevice;
    @Mock
    private TransactionService transactionService;
    @Mock
    private WalletService walletService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private MoneyTransferService moneyTransferService;

    private User sender;
    private User reciver;
    private Wallet senderWallet;
    private Wallet reciverWallet;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setEmail("sender@email");

        reciver = new User();
        reciver.setEmail("reciver@email");
        reciver.setId(2L);


        senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setUser(sender);
        senderWallet.setBalance(new BigDecimal(500));

        reciverWallet = new Wallet();
        reciverWallet.setId(2L);
        reciverWallet.setBalance(new BigDecimal(500));
        reciverWallet.setUser(reciver);

        transaction = new Transaction();
        transaction.setId(1L);
    }

    @Test
    void transferMoneySucesfully() {
        BigDecimal amount = new BigDecimal(100);
        String desc = "Ble BLeBle BLe";
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.of(sender));
        when(userSrevice.findUserByEmail("reciver@email")).thenReturn(Optional.of(reciver));
        when(walletService.getWalletByUser(sender)).thenReturn(senderWallet);
        when(walletService.getWalletByUser(reciver)).thenReturn(reciverWallet);
        when(transactionService.createTranscation(any(Wallet.class), any(Wallet.class), any(BigDecimal.class), any(TransactionType.class)
                , anyString())).thenReturn(transaction);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = moneyTransferService.transferMoney
                ("sender@email", "reciver@email", amount, desc);
        assertNotNull(result);
        assertEquals(new BigDecimal(400), senderWallet.getBalance());
        assertEquals(new BigDecimal(600), reciverWallet.getBalance());
        verify(userSrevice).findUserByEmail("sender@email");
        verify(userSrevice).findUserByEmail("reciver@email");
        verify(walletService).getWalletByUser(sender);
        verify(walletService).getWalletByUser(reciver);
        verify(transactionService).createTranscation(senderWallet, reciverWallet, amount, TransactionType.TRANSFER, desc);
        verify(walletRepository, times(2)).save(any(Wallet.class));

    }

    @Test
    void shouldShowErrorOnZero() {
        BigDecimal zeroamount = BigDecimal.ZERO;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> moneyTransferService.transferMoney("sender@email", "reciver@email", zeroamount, "Test")
        );
        assertEquals("Amount should be greater than 0", exception.getMessage());

    }

    @Test
    void shouldThrowExcpetionWhenAmountIsNeagtive() {
        BigDecimal zeroamount = new BigDecimal(-100);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> moneyTransferService.transferMoney("sender@email", "reciver@email", zeroamount, "Test")
        );
        assertEquals("Amount should be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowErrorWhenSenderNotFound() {
        BigDecimal amount = new BigDecimal(100);
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                moneyTransferService.transferMoney("sender@email", "reciver@email", amount, "test"));
        assertEquals("User not found", exception.getMessage());
        verify(userSrevice).findUserByEmail("sender@email");
        verify(walletService, never()).getWalletByUser(any(User.class));

    }

    @Test
    void shouldThrowErrorWhenReciverNotFound() {
        BigDecimal amount = new BigDecimal(100);
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.of(sender));
        when(userSrevice.findUserByEmail("reciver@email")).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                moneyTransferService.transferMoney("sender@email", "reciver@email", amount, "test"));
        assertEquals("User not found", exception.getMessage());
        verify(userSrevice).findUserByEmail("reciver@email");
        verify(walletService, never()).getWalletByUser(any(User.class));
    }
    @Test
    void shouldThrowErrorWhenSelfTransfer(){
        BigDecimal amount=new BigDecimal(100);
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.of(sender));
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.of(sender));

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->moneyTransferService
                .transferMoney("sender@email","sender@email",amount,"test"));
        assertEquals(exception.getMessage(),"cannot do self transfer");

    }
    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficinet(){
        BigDecimal amount=new BigDecimal(1000);
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.of(sender));
        when(userSrevice.findUserByEmail("reciver@email")).thenReturn(Optional.of(reciver));
        when(walletService.getWalletByUser(sender)).thenReturn(senderWallet);
        when(walletService.getWalletByUser(reciver)).thenReturn(reciverWallet);
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->moneyTransferService.
                transferMoney("sender@email","reciver@email",amount,"Test"));
        assertEquals(exception.getMessage(),"insufficent balance");
    }
    @Test
    void shouldThrowExceptionWhenTranscationFailed(){
        BigDecimal amount = new BigDecimal(100);
        String desc = "Ble BLeBle BLe";
        when(userSrevice.findUserByEmail("sender@email")).thenReturn(Optional.of(sender));
        when(userSrevice.findUserByEmail("reciver@email")).thenReturn(Optional.of(reciver));
        when(walletService.getWalletByUser(sender)).thenReturn(senderWallet);
        when(walletService.getWalletByUser(reciver)).thenReturn(reciverWallet);
        when(transactionService.createTranscation(any(Wallet.class), any(Wallet.class), any(BigDecimal.class), any(TransactionType.class)
                , anyString())).thenReturn(transaction);
        when(walletRepository.save(any(Wallet.class))).thenThrow(new RuntimeException("Database Error"));
        RuntimeException exception=assertThrows(RuntimeException.class,()->moneyTransferService
                .transferMoney("sender@email","reciver@email",amount,desc));
        assertTrue(exception.getMessage().contains("Transaction failed"));
verify(transactionService).updateTranscation(transaction,TranscationStatus.FAILED);
    }

}