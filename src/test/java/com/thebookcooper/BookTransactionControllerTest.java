package com.thebookcooper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.BookTransaction;
import com.thebookcooper.dao.BookTransactionDAO;
import com.thebookcooper.controller.BookTransactionController;
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
public class BookTransactionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookTransactionDAO bookTransactionDAO;

    @Mock
    private DatabaseConnectionManager dcm;

    @InjectMocks
    private BookTransactionController bookTransactionController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookTransactionController).build();
    }

    @Test
    public void testGetTransactionByIdDatabaseError() throws Exception {
        long transactionId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/book-transactions/{id}", transactionId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving transaction")));
    }

    @Test
    public void testCountTransactionsDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/book-transactions/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving transaction count")));
    }

    @Test
    public void testCreateTransactionDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/book-transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"buyerId\":1,\"sellerId\":2,\"transactionPrice\":15.0,\"transactionStatus\":\"Completed\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to process the transaction")));
    }

    @Test
    public void testUpdateTransactionDatabaseError() throws Exception {
        long transactionId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/book-transactions/update/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"buyerId\":1,\"sellerId\":2,\"transactionPrice\":15.0,\"transactionStatus\":\"Completed\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the transaction")));
    }

    @Test
    public void testDeleteTransactionDatabaseError() throws Exception {
        long transactionId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/book-transactions/delete/{id}", transactionId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to delete the transaction")));
    }
}
