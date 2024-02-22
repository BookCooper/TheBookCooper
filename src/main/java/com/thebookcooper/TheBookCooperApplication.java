package com.thebookcooper;

import com.thebookcooper.dao.*;
import com.thebookcooper.model.*;
import com.thebookcooper.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import java.util.List;

@SpringBootApplication
@RestController
public class TheBookCooperApplication {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432,
            "thebookcooper", "BCdev", "password");

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
    public User getUserById(@PathVariable("id") int id) {
        try (Connection connection = dcm.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider creating and returning a custom error object or message
        }
        return null; // Or return an appropriate response/entity indicating not found or error
    }


    @GetMapping("/books/count")
    public String countBooks() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM book_info")) {
            if (resultSet.next()) {
                return "Number of books: " + resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving book count";
        }
        return "No books found";
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookInfoDAO infoDAO = new BookInfoDAO(connection);
            return infoDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider creating and returning a custom error object or message
        }
        return null; // Or return an appropriate response/entity indicating not found or error
    }

    public static void main(String[] args) {
        SpringApplication.run(TheBookCooperApplication.class, args);
    }
}
