package com.banking.banking_system.service;

import com.banking.banking_system.dto.TransactionResponseDTO;
import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.enums.TransactionType;
import com.banking.banking_system.enums.TranscationStatus;
import com.banking.banking_system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction createTranscation(Wallet fromWallet, Wallet toWallet,
                                         BigDecimal amount, TransactionType type, String description){
        Transaction transcation =new Transaction(fromWallet,toWallet,amount,type,description);
        return transactionRepository.save(transcation);
    }
    @Transactional
    public void updateTranscation(Transaction transcation, TranscationStatus status){
        transcation.setStatus(status);
        transactionRepository.save(transcation);
    }
    public List<Transaction> getTransferHistory(Wallet wallet){
        return transactionRepository.findTransactionsByWallet(wallet.getId());
    }
    public List<Transaction> getTranscationbyDateRange(Wallet wallet,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate){
        return transactionRepository.findTransactionsbyWalletAndDateRange(wallet.getId(),startDate,endDate);
    }
    public TransactionResponseDTO convertToDto(Transaction transaction){
        TransactionResponseDTO dto=new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDescription(transaction.getDescreption());
        dto.setTimeStamp(transaction.getTimestamp());
        dto.setStatus(transaction.getStatus());

        if(transaction.getFromWallet()!=null){
            dto.setFromWalletId(transaction.getFromWallet().getId());
            dto.setFromUserEmail(transaction.getFromWallet().getUser().getEmail());
        }
        if(transaction.getToWallet()!=null){
            dto.setToWalletId(transaction.getToWallet().getId());
            dto.setToUserEmail(transaction.getToWallet().getUser().getEmail());
        }
        return dto;

    }
    public List<TransactionResponseDTO>convertToDtoList(List<Transaction> transactions){
        return transactions.stream().map(this::convertToDto).collect(java.util.stream.Collectors.toList());
    }

}
