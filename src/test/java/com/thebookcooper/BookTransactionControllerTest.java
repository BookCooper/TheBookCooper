package com.thebookcooper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.controller.BookTransactionController;
import com.thebookcooper.dao.BookTransactionDAO;
import com.thebookcooper.model.BookTransaction;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookTransactionController.class)
public class BookTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookTransactionDAO bookTransactionDAO;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username="admin", roles={"USER", "ADMIN"})
    public void getTransactionByIdTest() throws Exception {
        BookTransaction transaction = new BookTransaction();
        transaction.setTransactionId(1L);
        when(bookTransactionDAO.findById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/book-transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transaction)));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER", "ADMIN"})
    public void createTransactionTest() throws Exception {
        BookTransaction transaction = new BookTransaction();
        transaction.setTransactionId(1L);
        transaction.setBuyerId(1L);
        transaction.setSellerId(2L);
        transaction.setListingId(3L);
        transaction.setTransactionPrice(100.00);
        transaction.setTransactionStatus("Completed");

        String jsonContent = objectMapper.writeValueAsString(transaction);
        when(bookTransactionDAO.create(any(BookTransaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/book-transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonContent));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER", "ADMIN"})
    public void updateTransactionTest() throws Exception {
        BookTransaction transaction = new BookTransaction();
        transaction.setTransactionId(1L);
        transaction.setBuyerId(1L);
        transaction.setSellerId(2L);
        transaction.setListingId(3L);
        transaction.setTransactionPrice(120.00);
        transaction.setTransactionStatus("Updated");

        String jsonContent = objectMapper.writeValueAsString(transaction);
        when(bookTransactionDAO.update(any(BookTransaction.class))).thenReturn(transaction);

        mockMvc.perform(put("/book-transactions/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonContent));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER", "ADMIN"})
    public void deleteTransactionTest() throws Exception {
        doNothing().when(bookTransactionDAO).delete(1L);

        mockMvc.perform(delete("/book-transactions/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction with ID 1 deleted successfully"));
    }
        @Configuration
    @EnableWebSecurity
    @Profile("test")  // Ensure this profile is activated during tests
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()
                    .build();
        }
    }
}
