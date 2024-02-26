package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Map;

import com.thebookcooper.model.*;
import com.thebookcooper.dao.*;

import java.time.LocalDate;

@RestController
public class BookInfoController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    @PostMapping("/books/create")
    public Book createBook(@RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        Book newBook = new Book();

        try {
            Connection connection = dcm.getConnection();
            BookInfoDAO bookDAO = new BookInfoDAO(connection);

            //get inputs from user and assign them to a new object
            newBook.setTitle((String) inputMap.get("title"));
            //newBook.setPublishDate(Date.valueOf(LocalDate.now())); <-- set the date to be the current day
            newBook.setPublishDate(Date.valueOf((String) inputMap.get("publishDate"))); //date has to be of form "YYYY-MM-DD"
            newBook.setAuthor((String) inputMap.get("author"));
            newBook.setGenre((String) inputMap.get("genre"));
            newBook.setBookCondition((String) inputMap.get("bookCondition"));
            newBook.setPrice((double) inputMap.get("price"));

            Object isbnObject = inputMap.get("isbn");
            if (isbnObject instanceof Integer) {
                newBook.setISBN(((Integer) isbnObject).longValue()); // Convert Integer to Long
            } else if (isbnObject instanceof Long) {
                newBook.setISBN((Long) isbnObject);
            } else {
                throw new IllegalArgumentException("ISBN must be a number");
            }

            //calls create function from dao/BookInfoDAO to insert listing
            return bookDAO.create(newBook);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the book", e);
        }
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

    @PutMapping("/books/update/{id}")
    public Book updateBook(@PathVariable("id") long id, @RequestBody String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map inputMap = objectMapper.readValue(json, Map.class);

        Book updatedBook = new Book();
        try {
            Connection connection = dcm.getConnection();
            BookInfoDAO infoDAO = new BookInfoDAO(connection);

            //get inputs from user and assign them to a new object
            updatedBook.setBookId(id);
            updatedBook.setTitle((String) inputMap.get("title"));
            updatedBook.setISBN((int) inputMap.get("isbn"));
            updatedBook.setPublishDate(Date.valueOf((String) inputMap.get("publishDate"))); //date has to be of form "YYYY-MM-DD"
            updatedBook.setAuthor((String) inputMap.get("author"));
            updatedBook.setGenre((String) inputMap.get("genre"));
            updatedBook.setBookCondition((String) inputMap.get("bookCondition"));
            updatedBook.setPrice((double) inputMap.get("price"));

            //calls update function from dao/BookInfoDAO to update listing
            return infoDAO.update(updatedBook);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update the book", e);
        }
    }

    @DeleteMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookInfoDAO infoDAO = new BookInfoDAO(connection);
            infoDAO.delete(id);
            return "Book deleted";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete the book", e);
        }
    }
}
