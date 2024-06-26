package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.Map;

import com.thebookcooper.model.User;
import com.thebookcooper.dao.UserDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@CrossOrigin
@RequestMapping("/users") // Base path
public class UserController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    private User findUserById(long id) throws SQLException {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findById(id);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countUsers() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users")) {
            if (resultSet.next()) {
                return new ResponseEntity<>("Number of users: " + resultSet.getInt(1), HttpStatus.OK);
            }
            return new ResponseEntity<>("No users found", HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving user count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            User user = findUserById(id);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserById(@PathVariable("email") String email) {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.findByEmail(email);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            User newUser = new User();
            Connection connection = dcm.getConnection();
            UserDAO userDAO = new UserDAO(connection);

            newUser.setUserName((String) inputMap.get("userName"));
            newUser.setEmail((String) inputMap.get("email"));
            newUser.setBBucksBalance(Double.parseDouble(inputMap.get("bBucksBalance").toString()));
            newUser.setCreationDate(new Timestamp(System.currentTimeMillis()));
            newUser.setLastLogin(new Timestamp(System.currentTimeMillis()));
            
            User createdUser = userDAO.create(newUser);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create the user", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format for bBucksBalance", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            UserDAO userDAO = new UserDAO(connection);

            User updatedUser = findUserById(id);
            updatedUser.setUserName((String) inputMap.get("userName"));
            updatedUser.setEmail((String) inputMap.get("email"));
            updatedUser.setBBucksBalance(Double.parseDouble(inputMap.get("bBucksBalance").toString()));
            updatedUser.setLastLogin(new Timestamp(System.currentTimeMillis()));
            
            User user = userDAO.update(updatedUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the user", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format for bBucksBalance", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            userDAO.delete(id);
            return new ResponseEntity<>("User with id " + id + " has been deleted", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting user with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
