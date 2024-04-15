package com.thebookcooper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.thebookcooper.model.StoreItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class StoreItemTest {

    private StoreItem storeItem;

    @BeforeEach
    void setUp() {
        storeItem = new StoreItem();
        storeItem.setStoreId(1L);
        storeItem.setItem("Book");
        storeItem.setItemPrice(new BigDecimal("19.99"));
        storeItem.setSpecialOffer("20% off");
        storeItem.setItemDescription("A popular science fiction novel");
    }

    @Test
    void testGetStoreId() {
        assertEquals(1L, storeItem.getStoreId(), "Store ID should match the set value");
    }

    @Test
    void testGetItem() {
        assertEquals("Book", storeItem.getItem(), "Item should match the set value");
    }

    @Test
    void testGetItemPrice() {
        assertEquals(new BigDecimal("19.99"), storeItem.getItemPrice(), "Item price should match the set value");
    }

    @Test
    void testGetSpecialOffer() {
        assertEquals("20% off", storeItem.getSpecialOffer(), "Special offer should match the set value");
    }

    @Test
    void testGetItemDescription() {
        assertEquals("A popular science fiction novel", storeItem.getItemDescription(), "Item description should match the set value");
    }
}
