package com.banking.banking_system.service;

import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.enums.TransactionType;
import com.banking.banking_system.enums.TranscationStatus;
import com.banking.banking_system.repository.TransactionRepository;
import com.banking.banking_system.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MoneyTransferService {
    @Autowired
    private UserSrevice userSrevice;
    @Autowired
    private WalletService walletService;
    @Autowired
    private TransactionService transcationService;
    @Autowired
    private TransactionRepository transcationRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Transaction transferMoney(String senderEmail, String reciverEmail, BigDecimal amount, String description){
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Amount sholud be greater then 0");
        }
        User sender=userSrevice.findUserByEmail(senderEmail).orElseThrow(()->new IllegalArgumentException("User not found"));
        User receiver=userSrevice.findUserByEmail(reciverEmail).orElseThrow(()->new IllegalArgumentException("User not found"));
        if(sender.getId().equals(receiver.getId())){
            throw new IllegalArgumentException("cannot do self transfer");
        }
        Wallet senderWallet=walletService.getWalletByUser(sender);
        Wallet reciverrWallet=walletService.getWalletByUser(receiver);
        if(senderWallet.getBalance().compareTo(amount)<0){
            throw new IllegalArgumentException("insufficent balance");
        }
        Transaction transcation=transcationService.createTranscation(senderWallet,reciverrWallet,amount,TransactionType.TRANSFER,description);
        try{
            senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
            reciverrWallet.setBalance(reciverrWallet.getBalance().add(amount));
            walletRepository.save(senderWallet);
            walletRepository.save(reciverrWallet);
            transcationService.updateTranscation(transcation, TranscationStatus.COMPLETED);
            return transcation;

        }
        catch (Exception e){
            transcationService.updateTranscation(transcation,TranscationStatus.FAILED);
            throw new RuntimeException("TRansation failed"+e);
        }

    }

}
