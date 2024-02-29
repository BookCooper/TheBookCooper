package com.thebookcooper.util;

public interface BookTransactionDTO {
    long getTransactionId();
    long getBuyerId();
    long getSellerId();
    long getListingId();
    double getTransactionPrice();
    String getTransactionStatus();
}
