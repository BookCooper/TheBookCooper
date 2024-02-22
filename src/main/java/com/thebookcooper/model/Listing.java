package com.thebookcooper.model;

import com.thebookcooper.util.ListingsDTO;

import java.sql.Timestamp;

public class Listing implements ListingsDTO {

    private long listingId;
    private long userId; //user that listed the book
    private long bookId;
    private String listingStatus;
    private Timestamp listingDate;

    @Override
    public long getListingId() {
        return listingId;
    }

    public void setListingId(long listingId) {
        this.listingId = listingId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getListingStatus() { return listingStatus; }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

    public Timestamp getListingDate() { return listingDate; }

    public void setListingDate(Timestamp listingDate) {
        this.listingDate = listingDate;
    }


    @Override
    public String toString() {
        return "Book{" +
                "ListingId=" + listingId +
                ", userId=" + userId + '\'' +
                ", bookId=" + bookId + '\'' +
                ", listingStatus=" + listingStatus +
                ", listingDate=" + listingDate +
                '}';
    }
}
