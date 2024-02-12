package com.thebookcooper.controller;

import com.thebookcooper.dao.DatabaseConnectionManager;
import com.thebookcooper.model.User;
import com.thebookcooper.dao.UserDAO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class UserController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", 5555, "thebookcooper", "BCdev", "password");

    @PostMapping("/users")
    public User createUser(@RequestBody User newUser) {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            // Assuming newUser already contains all the necessary fields set from the request body
            return userDAO.create(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
            // In a real application, you would handle this exception more gracefully
            throw new RuntimeException("Failed to create the user", e);
        }
    }
}
