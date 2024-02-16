package com.thebookcooper.model;

import com.thebookcooper.util.DataTransferObject;

import java.sql.Date;

public class Book implements DataTransferObject {

    private long bookId;
    private String title;
    private int isbn;
    private Date publishDate;
    private String author;
    private String genre;
    private String bookStatus;


    @Override
    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishDate() { return publishDate; }
    public void setDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getISBN() { return isbn; }

    public void setISBN(int isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() { return genre; }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBookStatus() { return bookStatus; }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    @Override
    public String toString() {
        return "Book{" +
                "BookID=" + bookId +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publish date='" + publishDate + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", book status=" + bookStatus +
                '}';
    }
}
