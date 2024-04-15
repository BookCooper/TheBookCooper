package com.thebookcooper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thebookcooper.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserName("JohnDoe");
        user.setEmail("john.doe@example.com");
        user.setBBucksBalance(150.00);
        user.setCreationDate(Timestamp.from(Instant.now()));
        user.setLastLogin(Timestamp.from(Instant.now()));
    }

    @Test
    void testGetUserId() {
        assertEquals(1L, user.getUserId(), "User ID should match the set value");
    }

    @Test
    void testGetUserName() {
        assertEquals("JohnDoe", user.getUserName(), "User name should match the set value");
    }

    @Test
    void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail(), "Email should match the set value");
    }

    @Test
    void testGetBBucksBalance() {
        assertEquals(150.00, user.getBBucksBalance(), 0.01, "B-Bucks Balance should match the set value");
    }

    @Test
    void testGetCreationDate() {
        long expectedTime = Timestamp.from(Instant.now()).getTime();
        long actualTime = user.getCreationDate().getTime();
        assertEquals(expectedTime, actualTime, 500, "Creation date should be within 500 milliseconds of the expected time");
    }

    @Test
    void testGetLastLogin() {
        long expectedTime = Timestamp.from(Instant.now()).getTime();
        long actualTime = user.getLastLogin().getTime();
        assertEquals(expectedTime, actualTime, 500, "Last login date should be within 500 milliseconds of the expected time");
    }
}
