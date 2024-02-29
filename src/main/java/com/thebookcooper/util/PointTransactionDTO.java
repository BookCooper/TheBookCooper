package com.thebookcooper.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface PointTransactionDTO {
    
    long getBbTransactionId();
    long getUserId();
    String getTransactionType();
    BigDecimal getAmount();
    Timestamp getTransactionDate();
    BigDecimal getCurrentBalance();
}
