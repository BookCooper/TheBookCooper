package com.thebookcooper.model;

import com.thebookcooper.util.BookTagDTO;

public class BookTag implements BookTagDTO{

    private long tagId;
    private String tagName;
    private long bookId; // Reference to Book

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "BookTag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}
