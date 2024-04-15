package com.thebookcooper;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thebookcooper.model.BookSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

public class BookSearchTest {

    private BookSearch bookSearch;

    @BeforeEach
    void setUp() {
        bookSearch = new BookSearch();
        bookSearch.setSearchId(1L);
        bookSearch.setUserId(100L);
        bookSearch.setSearchQuery("Java Concurrency");
        bookSearch.setSearchDate(Timestamp.from(Instant.now()));
    }

    @Test
    void testGetSearchId() {
        assertEquals(1L, bookSearch.getSearchId(), "Search ID should match the set value");
    }

    @Test
    void testGetUserId() {
        assertEquals(100L, bookSearch.getUserId(), "User ID should match the set value");
    }

    @Test
    void testGetSearchQuery() {
        assertEquals("Java Concurrency", bookSearch.getSearchQuery(), "Search query should match the set value");
    }

    @Test
    void testGetSearchDate() {
        // Example of asserting not null because exact timestamp matching might not be relevant
        // If exact timestamp matching is needed, store the timestamp from Instant.now() in the setUp method and compare here.
        assertEquals(Timestamp.from(Instant.now()).getTime(), bookSearch.getSearchDate().getTime(), 1000, "Search date should be within 1000 milliseconds of the set time");
    }
}
