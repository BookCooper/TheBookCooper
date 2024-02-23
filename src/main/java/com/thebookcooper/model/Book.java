package com.thebookcooper.model;

import com.thebookcooper.util.BookDTO;
import java.sql.Date;

public class Book implements BookDTO {

    private long bookId;
    private String title;
    private int isbn;
    private Date publishDate;
    private String author;
    private String genre;
    private String book_condition;
    private double price;

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

    public void setPublishDate(Date publishDate) {
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

    public String getBookCondition() { return book_condition; }

    public void setBookCondition(String book_condition) { this.book_condition = book_condition; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Book{" +
                "BookID=" + bookId +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publish date='" + publishDate + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", condition=" + book_condition +
                ", price=" + price +
                '}';
    }
}
