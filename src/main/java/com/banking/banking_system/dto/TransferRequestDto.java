package com.banking.banking_system.dto;

import java.math.BigDecimal;

public class TransferRequestDto {
    private String reciverEmail;
    private BigDecimal amount;
    private String description;

    public TransferRequestDto() {
    }

    public TransferRequestDto(String reciverEmail, BigDecimal amount, String description) {
        this.reciverEmail = reciverEmail;
        this.amount = amount;
        this.description = description;
    }

    public String getReciverEmail() {
        return reciverEmail;
    }

    public void setReciverEmail(String reciverEmail) {
        this.reciverEmail = reciverEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
