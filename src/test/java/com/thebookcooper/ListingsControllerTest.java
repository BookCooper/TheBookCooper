package com.thebookcooper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.Listing;
import com.thebookcooper.dao.ListingsDAO;
import com.thebookcooper.controller.ListingsController;
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
public class ListingsControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ListingsDAO listingsDAO;

    @Mock
    private DatabaseConnectionManager dcm;

    @InjectMocks
    private ListingsController listingsController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(listingsController).build();
    }

    @Test
    public void testCountListingsDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/listings/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving listing count")));
    }

    @Test
    public void testGetListingByIdDatabaseError() throws Exception {
        long listingId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/listings/{id}", listingId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving listing")));
    }

    @Test
    public void testCreateListingDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/listings/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"bookId\":2,\"listingStatus\":\"Available\",\"bookCondition\":\"New\",\"price\":15.50}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create the listing")));
    }

    @Test
    public void testUpdateListingDatabaseError() throws Exception {
        long listingId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/listings/update/{id}", listingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"bookId\":2,\"listingStatus\":\"Available\",\"bookCondition\":\"New\",\"price\":15.50}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the listing")));
    }

    @Test
    public void testDeleteListingDatabaseError() throws Exception {
        long listingId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/listings/delete/{id}", listingId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error deleting listing with id")));
    }
}
