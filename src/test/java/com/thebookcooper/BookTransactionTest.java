package com.thebookcooper;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thebookcooper.model.BookTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

public class BookTransactionTest {

    private BookTransaction bookTransaction;

    @BeforeEach
    void setUp() {
        bookTransaction = new BookTransaction();
        bookTransaction.setTransactionId(1L);
        bookTransaction.setBuyerId(2L);
        bookTransaction.setSellerId(3L);
        bookTransaction.setTransactionDate(Timestamp.from(Instant.now()));
        bookTransaction.setListingId(4L);
        bookTransaction.setTransactionPrice(50.00);
        bookTransaction.setTransactionStatus("Completed");
    }

    @Test
    void testGetTransactionId() {
        assertEquals(1L, bookTransaction.getTransactionId(), "Transaction ID should match the set value");
    }

    @Test
    void testGetBuyerId() {
        assertEquals(2L, bookTransaction.getBuyerId(), "Buyer ID should match the set value");
    }

    @Test
    void testGetSellerId() {
        assertEquals(3L, bookTransaction.getSellerId(), "Seller ID should match the set value");
    }

    @Test
    void testGetTransactionDate() {
        // Using a range to validate the timestamp due to possible slight differences when creating and getting it
        long expectedTime = Timestamp.from(Instant.now()).getTime();
        long actualTime = bookTransaction.getTransactionDate().getTime();
        assertEquals(expectedTime, actualTime, 500, "Transaction date should be within 500 milliseconds of the expected time");
    }

    @Test
    void testGetListingId() {
        assertEquals(4L, bookTransaction.getListingId(), "Listing ID should match the set value");
    }

    @Test
    void testGetTransactionPrice() {
        assertEquals(50.00, bookTransaction.getTransactionPrice(), 0.01, "Transaction price should match the set value");
    }

    @Test
    void testGetTransactionStatus() {
        assertEquals("Completed", bookTransaction.getTransactionStatus(), "Transaction status should match the set value");
    }
}
