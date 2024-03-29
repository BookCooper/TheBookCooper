package com.thebookcooper.util;

public interface ListingsDTO {

    long getListingId();
    long getUserId();
    long getBookId();
    String getBookCondition();
    double getPrice();
    String getListingStatus(); 
}
