package com.thebookcooper.model;

import com.thebookcooper.util.PointTransactionDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PointTransaction implements PointTransactionDTO {

    private long bbTransactionId;
    private long userId;
    private String transactionType;
    private BigDecimal amount;
    private Timestamp transactionDate;
    private BigDecimal currentBalance;

    @Override
    public long getBbTransactionId() {
        return bbTransactionId;
    }

    public void setBbTransactionId(long bbTransactionId) {
        this.bbTransactionId = bbTransactionId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public String toString() {
        return "PointTransaction{" +
                "bbTransactionId=" + bbTransactionId +
                ", userId=" + userId +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", currentBalance=" + currentBalance +
                '}';
    }
}
