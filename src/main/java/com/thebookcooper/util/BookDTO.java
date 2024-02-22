package com.thebookcooper.util;

public interface BookDTO {

    long getBookId();
    String getTitle();
    int getISBN();
    String getAuthor();
    String getGenre();
    String getBookCondition();
    double getPrice();
}