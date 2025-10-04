package com.banking.banking_system.entity;

import com.banking.banking_system.enums.TransactionType;
import com.banking.banking_system.enums.TranscationStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_Wallet_id")
    private Wallet fromWallet;

    @ManyToOne
    @JoinColumn(name = "to_Wallet_id")
    private Wallet toWallet;

    @Column(nullable = false,precision = 19,scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TranscationStatus status;
    @Column(length = 500)
    private String descreption;
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction() {
        this.timestamp=LocalDateTime.now();
        this.status=TranscationStatus.PENDING;
    }

    public Transaction(Wallet wallet, BigDecimal amount, TransactionType type, String descreption) {
      this();
      if(type==TransactionType.DEPOSIT){
          this.toWallet=wallet;
          this.fromWallet=null;
      }else if (type==TransactionType.WITHDRAWL){
          this.toWallet=null;
          this.fromWallet=wallet;
      }
        this.amount = amount;
        this.type = type;

        this.descreption = descreption;
    }

    public Transaction(Wallet fromWallet, Wallet toWallet, BigDecimal amount, TransactionType type, String descreption) {
        this();
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.amount = amount;
        this.type=TransactionType.TRANSFER;
        this.descreption=descreption;
    }

    public Transaction(Wallet fromWallet, Wallet toWallet, BigDecimal amount, TransactionType type, TranscationStatus status, String descreption) {
        this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.descreption = descreption;
        this.timestamp=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(Wallet fromWallet) {
        this.fromWallet = fromWallet;
    }

    public Wallet getToWallet() {
        return toWallet;
    }

    public void setToWallet(Wallet toWallet) {
        this.toWallet = toWallet;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TranscationStatus getStatus() {
        return status;
    }

    public void setStatus(TranscationStatus status) {
        this.status = status;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
