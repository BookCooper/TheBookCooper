package com.thebookcooper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thebookcooper.model.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

public class ListingTest {

    private Listing listing;

    @BeforeEach
    void setUp() {
        listing = new Listing();
        listing.setListingId(1L);
        listing.setUserId(2L);
        listing.setBookId(3L);
        listing.setPrice(99.99);
        listing.setListingStatus("Active");
        listing.setListingDate(Timestamp.from(Instant.now()));
        listing.setBookCondition("New");
    }

    @Test
    void testGetListingId() {
        assertEquals(1L, listing.getListingId(), "Listing ID should match the set value");
    }

    @Test
    void testGetUserId() {
        assertEquals(2L, listing.getUserId(), "User ID should match the set value");
    }

    @Test
    void testGetBookId() {
        assertEquals(3L, listing.getBookId(), "Book ID should match the set value");
    }

    @Test
    void testGetPrice() {
        assertEquals(99.99, listing.getPrice(), "Price should match the set value");
    }

    @Test
    void testGetListingStatus() {
        assertEquals("Active", listing.getListingStatus(), "Listing status should match the set value");
    }

    @Test
    void testGetListingDate() {
        // Using a range to validate the timestamp due to possible slight differences when creating and getting it
        long expectedTime = Timestamp.from(Instant.now()).getTime();
        long actualTime = listing.getListingDate().getTime();
        assertEquals(expectedTime, actualTime, 500, "Listing date should be within 500 milliseconds of the expected time");
    }

    @Test
    void testGetBookCondition() {
        assertEquals("New", listing.getBookCondition(), "Book condition should match the set value");
    }
}
