package com.thebookcooper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.BookSearch;
import com.thebookcooper.dao.BookSearchDAO;
import com.thebookcooper.controller.BookSearchController;
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

@ExtendWith(SpringExtension.class)
public class BookSearchControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookSearchDAO bookSearchDAO;

    @Mock
    private DatabaseConnectionManager dcm;

    @InjectMocks
    private BookSearchController bookSearchController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookSearchController).build();
    }

    @Test
    public void testCreateBookSearchDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/book-searches/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"searchQuery\":\"Fiction\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create the book search")));
    }

    @Test
    public void testGetBookSearchByIdDatabaseError() throws Exception {
        long searchId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/book-searches/{id}", searchId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving book search")));
    }

    @Test
    public void testUpdateBookSearchDatabaseError() throws Exception {
        long searchId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/book-searches/update/{id}", searchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1, \"searchQuery\":\"Updated Search\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the book search")));
    }

    @Test
    public void testCountBookSearchesDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/book-searches/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving book search count")));
    }

    @Test
    public void testDeleteBookSearchDatabaseError() throws Exception {
        long searchId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/book-searches/delete/{id}", searchId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to delete the book search")));
    }
}
