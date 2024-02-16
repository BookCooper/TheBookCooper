package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.dao.DatabaseConnectionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

@RestController
public class BookInfoController {

    @PostMapping("/users/create")
    public User createBook(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

        User newUser = new User();
    
        try{ 
            Connection connection = dcm.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            newUser.setUserName((String) inputMap.get("userName"));
            newUser.setPassword((String) inputMap.get("password"));
            newUser.setEmail((String) inputMap.get("email"));
            newUser.setBBucksBalance((double) inputMap.get("bBucksBalance"));
            newUser.setCreationDate(new Timestamp(System.currentTimeMillis()));
            newUser.setLastLogin(new Timestamp(System.currentTimeMillis()));
            return userDAO.create(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the user", e);
        }
    }
}
