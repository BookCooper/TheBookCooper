package com.thebookcooper.model;

import java.sql.Timestamp;

public class BookSearch {

    private long searchId;
    private long userId;
    private String searchQuery;
    private Timestamp searchDate;

    // Getters and Setters
    public long getSearchId() {
        return searchId;
    }

    public void setSearchId(long searchId) {
        this.searchId = searchId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Timestamp getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Timestamp searchDate) {
        this.searchDate = searchDate;
    }

    @Override
    public String toString() {
        return "BookSearch{" +
                "searchId=" + searchId +
                ", userId=" + userId +
                ", searchQuery='" + searchQuery + '\'' +
                ", searchDate=" + searchDate +
                '}';
    }
}
