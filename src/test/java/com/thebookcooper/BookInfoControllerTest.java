package com.thebookcooper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.Book;
import com.thebookcooper.dao.BookInfoDAO;
import com.thebookcooper.controller.BookInfoController;
import com.thebookcooper.dao.DatabaseConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class BookInfoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookInfoDAO bookInfoDAO;

    @Mock
    private DatabaseConnectionManager dcm;

    @InjectMocks
    private BookInfoController bookInfoController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookInfoController).build();
    }


    @Test
    public void testDatabaseErrorOnCountBooks() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/books/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving book count")));
    }

    @Test
    public void testDatabaseErrorOnGetBookById() throws Exception {
        long bookId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving book")));
    }

    @Test
    public void testCreateBookDatabaseError() throws Exception {
        Book book = new Book();
        book.setTitle("New Book");
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create the book")));
    }

    @Test
    public void testUpdateBookDatabaseError() throws Exception {
        long bookId = 1L;
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle("Updated Title");
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/books/update/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the book")));
    }

    @Test
    public void testDeleteBookDatabaseError() throws Exception {
        long bookId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/books/delete/{id}", bookId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to delete the book")));
    }
}