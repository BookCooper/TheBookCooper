package com.thebookcooper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thebookcooper.model.PointTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

public class PointTransactionTest {

    private PointTransaction pointTransaction;

    @BeforeEach
    void setUp() {
        pointTransaction = new PointTransaction();
        pointTransaction.setBbTransactionId(1L);
        pointTransaction.setUserId(2L);
        pointTransaction.setTransactionType("Credit");
        pointTransaction.setAmount(new BigDecimal("100.00"));
        pointTransaction.setTransactionDate(Timestamp.from(Instant.now()));
        pointTransaction.setCurrentBalance(new BigDecimal("500.00"));
    }

    @Test
    void testGetBbTransactionId() {
        assertEquals(1L, pointTransaction.getBbTransactionId(), "BbTransaction ID should match the set value");
    }

    @Test
    void testGetUserId() {
        assertEquals(2L, pointTransaction.getUserId(), "User ID should match the set value");
    }

    @Test
    void testGetTransactionType() {
        assertEquals("Credit", pointTransaction.getTransactionType(), "Transaction type should match the set value");
    }

    @Test
    void testGetAmount() {
        assertEquals(new BigDecimal("100.00"), pointTransaction.getAmount(), "Amount should match the set value");
    }

    @Test
    void testGetTransactionDate() {
        long expectedTime = Timestamp.from(Instant.now()).getTime();
        long actualTime = pointTransaction.getTransactionDate().getTime();
        assertEquals(expectedTime, actualTime, 500, "Transaction date should be within 500 milliseconds of the expected time");
    }

    @Test
    void testGetCurrentBalance() {
        assertEquals(new BigDecimal("500.00"), pointTransaction.getCurrentBalance(), "Current balance should match the set value");
    }
}
