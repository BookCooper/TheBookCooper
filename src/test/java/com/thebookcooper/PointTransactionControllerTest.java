package com.thebookcooper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.PointTransaction;
import com.thebookcooper.dao.PointTransactionDAO;
import com.thebookcooper.controller.PointTransactionController;
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
public class PointTransactionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PointTransactionDAO pointTransactionDAO;

    @Mock
    private DatabaseConnectionManager dcm;

    @InjectMocks
    private PointTransactionController pointTransactionController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pointTransactionController).build();
    }

    @Test
    public void testCountPointTransactionsDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/point-transactions/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving point transaction count")));
    }

    @Test
    public void testGetPointTransactionByIdDatabaseError() throws Exception {
        long transactionId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/point-transactions/{id}", transactionId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving point transaction")));
    }

    @Test
    public void testCreatePointTransactionDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/point-transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"transactionType\":\"Deposit\",\"amount\":100}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create the point transaction")));
    }

    @Test
    public void testUpdatePointTransactionDatabaseError() throws Exception {
        long transactionId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/point-transactions/update/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"transactionType\":\"Purchase\",\"amount\":50,\"currentBalance\":450}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the point transaction")));
    }

    @Test
    public void testDeletePointTransactionDatabaseError() throws Exception {
        long transactionId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/point-transactions/delete/{id}", transactionId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to delete the point transaction")));
    }
}
