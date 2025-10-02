package com.banking.banking_system.service;

import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.repository.UserRepository;
import com.banking.banking_system.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;

    public Wallet createWallet(User user){
        String accountNumber=genrateAccountNumber();
        Wallet wallet=new Wallet(accountNumber, BigDecimal.ZERO,user);
        return walletRepository.save(wallet);

    }
    private String genrateAccountNumber(){
        String accountNumber;
        do{
            long timeStamp=System.currentTimeMillis();
            int randomNum=(int)(Math.random()*1000);
            accountNumber="ACC"+timeStamp+String.format("%03d",randomNum);
        }while(walletRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
    public BigDecimal getBalance(User user){
        Wallet wallet=walletRepository.findByUser(user).orElseThrow(()->new RuntimeException("Wallet not found"));
        return wallet.getBalance();
    }
    public Wallet deposit(User user,BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("Amount should be greater then Zero");
        }
        Wallet wallet=walletRepository.findByUser(user).orElseThrow(()->new RuntimeException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        return walletRepository.save(wallet);
    }
    public Wallet withdraw(User user ,BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("Amount should be greater then Zero");

        }
        Wallet wallet=walletRepository.findByUser(user).orElseThrow(()->new RuntimeException("Wallet not found"));
        if(wallet.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insuffiecent balance");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        return walletRepository.save(wallet);
    }
    public Wallet getWalletByUser(User user){
        return walletRepository.findByUser(user).orElseThrow(()->new RuntimeException("User not found"));
    }

}
