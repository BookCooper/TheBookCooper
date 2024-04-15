package com.thebookcooper;

import com.thebookcooper.model.BookTag;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookTagTest {

    private BookTag bookTag;

    @BeforeEach
    void setUp() {
        bookTag = new BookTag();
        bookTag.setTagId(1L);
        bookTag.setTagName("Programming");
        bookTag.setBookId(101L);
    }

    @Test
    void testGetTagId() {
        assertEquals(1L, bookTag.getTagId(), "Tag ID should match the set value");
    }

    @Test
    void testGetTagName() {
        assertEquals("Programming", bookTag.getTagName(), "Tag name should match the set value");
    }

    @Test
    void testGetBookId() {
        assertEquals(101L, bookTag.getBookId(), "Book ID should match the set value");
    }
}
