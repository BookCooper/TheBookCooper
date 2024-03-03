package com.thebookcooper.util;

import java.math.BigDecimal;

public interface StoreItemDTO {
    
    long getStoreId();
    String getItem();
    BigDecimal getItemPrice();
    String getSpecialOffer();
    String getItemDescription();
}
