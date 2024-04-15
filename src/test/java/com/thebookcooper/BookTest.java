package com.thebookcooper;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import com.thebookcooper.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setISBN(1234567890123L);
        book.setPublishDate(Date.valueOf("2020-01-01"));
        book.setAuthor("Robert C. Martin");
        book.setGenre("Software Engineering");
        book.setBookCondition("New");
        book.setPrice(29.99);
    }

    @Test
    void testGetBookId() {
        assertEquals(1L, book.getBookId(), "Book ID should be 1");
    }

    @Test
    void testGetTitle() {
        assertEquals("Clean Code", book.getTitle(), "Book title should match");
    }

    @Test
    void testGetISBN() {
        assertEquals(1234567890123L, book.getISBN(), "Book ISBN should match");
    }

    @Test
    void testGetPublishDate() {
        assertEquals(Date.valueOf("2020-01-01"), book.getPublishDate(), "Publish date should match");
    }

    @Test
    void testGetAuthor() {
        assertEquals("Robert C. Martin", book.getAuthor(), "Author should match");
    }

    @Test
    void testGetGenre() {
        assertEquals("Software Engineering", book.getGenre(), "Genre should match");
    }

    @Test
    void testGetBookCondition() {
        assertEquals("New", book.getBookCondition(), "Book condition should match");
    }

    @Test
    void testGetPrice() {
        assertEquals(29.99, book.getPrice(), "Price should match");
    }
}
