package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebookcooper.dao.DatabaseConnectionManager;
import com.thebookcooper.model.User;
import com.thebookcooper.dao.UserDAO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;

@RestController
public class UserController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432,
            "thebookcooper", "BCdev", "password");

    @PostMapping("/users/create")
    public User createUser(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

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

    @GetMapping("/users/count")
    public String countUsers() {
        try (Connection connection = dcm.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users")) {
            if (resultSet.next()) {
                return "Number of users: " + resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving user count";
        }
        return "No users found";
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider creating and returning a custom error object or message
        }
        return null; // Or return an appropriate response/entity indicating not found or error
    }

    @PostMapping("/users/update/{id}")
    public User updateUser(@PathVariable("id") long id, @RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        User updatedUser = new User();
        try {
            Connection connection = dcm.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            updatedUser.setUserId(id);
            updatedUser.setUserName((String) inputMap.get("userName"));
            updatedUser.setPassword((String) inputMap.get("password"));
            updatedUser.setEmail((String) inputMap.get("email"));
            updatedUser.setBBucksBalance((double) inputMap.get("bBucksBalance"));
            updatedUser.setLastLogin(new Timestamp(System.currentTimeMillis()));
            return userDAO.update(updatedUser);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update the user", e);
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            userDAO.delete(id);
            return "User with id " + id + " has been deleted";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting user with id " + id;
        }
    }
}
