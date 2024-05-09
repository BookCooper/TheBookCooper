package com.thebookcooper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.BookTag;
import com.thebookcooper.dao.BookTagDAO;
import com.thebookcooper.controller.BookTagController;
import com.thebookcooper.dao.DatabaseConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
public class BookTagControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DatabaseConnectionManager dcm;

    @Mock
    private BookTagDAO bookTagDAO;

    @InjectMocks
    private BookTagController bookTagController;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(bookTagController).build();
    }

    @Test
    public void testGetBookTagByIdError() throws Exception {
        long tagId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/booktags/{id}", tagId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving book tag")));
    }

    @Test
    public void testCountBookTagsError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/booktags/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving book tag count")));
    }

    @Test
    public void testCreateBookTagError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/booktags/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookTag())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create the book tag")));
    }

    @Test
    public void testUpdateBookTagError() throws Exception {
        long tagId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/booktags/update/{id}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookTag())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the book tag")));
    }

    @Test
    public void testDeleteBookTagError() throws Exception {
        long tagId = 1L;
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/booktags/delete/{id}", tagId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to delete the book tag")));
    }
}
