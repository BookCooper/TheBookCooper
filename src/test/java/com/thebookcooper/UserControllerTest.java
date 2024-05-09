package com.thebookcooper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.model.User;
import com.thebookcooper.dao.UserDAO;
import com.thebookcooper.controller.UserController;
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
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DatabaseConnectionManager dcm;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(userController).build();
    }

    @Test
    public void testCountUsersDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/users/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving user count")));
    }

    @Test
    public void testGetUserByIdDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error retrieving user")));
    }

    @Test
    public void testCreateUserDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to create the user")));
    }

    @Test
    public void testUpdateUserDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(put("/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to update the user")));
    }

    @Test
    public void testDeleteUserDatabaseError() throws Exception {
        when(dcm.getConnection()).thenThrow(new SQLException("Database connection failed"));

        mockMvc.perform(delete("/users/delete/{id}", 1))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error deleting user with id")));
    }
}
