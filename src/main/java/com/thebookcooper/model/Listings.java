package com.thebookcooper.model;

import com.thebookcooper.util.DataTransferObject;

import java.sql.Timestamp;

public class Listings implements DataTransferObject {

    private long listingId;
    private long userId; //user that listed the book
    private long bookId;
    private int price;
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

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

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
                ", price=" + price + '\'' +
                ", listingStatus=" + listingStatus +
                ", listingDate=" + listingDate +
                '}';
    }
}
