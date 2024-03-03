package com.thebookcooper.model;


import com.thebookcooper.util.BookTransactionDTO;

import java.sql.Timestamp;


public class BookTransaction implements BookTransactionDTO{

    private long transactionId;
    private long buyerId;
    private long sellerId;
    private Timestamp transactionDate;
    private long listingId;
    private double transactionPrice;
    private String transactionStatus;

    // Getters and Setters
    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(long buyerId) {
        this.buyerId = buyerId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public long getListingId() {
        return listingId;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public String toString() {
        return "BookTransaction{" +
                "transactionId=" + transactionId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", transactionDate=" + transactionDate +
                ", listingId=" + listingId +
                ", transactionPrice=" + transactionPrice +
                ", transactionStatus='" + transactionStatus + '\'' +
                '}';
    }

}

