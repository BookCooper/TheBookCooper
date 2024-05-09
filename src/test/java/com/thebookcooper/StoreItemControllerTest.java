package com.thebookcooper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.StoreItem;
import com.thebookcooper.dao.StoreItemDAO;
import com.thebookcooper.controller.StoreItemController;
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
public class StoreItemControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private StoreItemDAO storeItemDAO;

    @Mock
    private DatabaseConnectionManager dcm;

    @InjectMocks
    private StoreItemController storeItemController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(storeItemController).build();
    }

    @Test
    public void testCreateStoreItemDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/store-items/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"item\":\"Laptop\",\"item_price\":\"999.99\",\"special_offer\":\"None\",\"item_description\":\"Latest model\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create store item")));
    }

    @Test
    public void testGetStoreItemByIdDatabaseError() throws Exception {
        long itemId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/store-items/{id}", itemId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving store item")));
    }

    @Test
    public void testUpdateStoreItemDatabaseError() throws Exception {
        long itemId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/store-items/update/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"item\":\"Laptop\",\"item_price\":\"1099.99\",\"special_offer\":\"Discount\",\"item_description\":\"Upgraded processor\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update store item")));
    }

    @Test
    public void testDeleteStoreItemDatabaseError() throws Exception {
        long itemId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/store-items/delete/{id}", itemId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to delete store item")));
    }
}
