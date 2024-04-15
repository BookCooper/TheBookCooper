package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Map;

import com.thebookcooper.model.BookSearch;
import com.thebookcooper.dao.BookSearchDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@RequestMapping("/book-searches") // Adjusted base path to match resource
public class BookSearchController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    @PostMapping("/create")
    public ResponseEntity<?> createBookSearch(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            BookSearch newSearch = new BookSearch();
            Connection connection = dcm.getConnection();
            BookSearchDAO searchDAO = new BookSearchDAO(connection);

            newSearch.setUserId(Long.parseLong(inputMap.get("userId").toString()));
            newSearch.setSearchQuery((String) inputMap.get("searchQuery"));
            newSearch.setSearchDate(new java.sql.Timestamp(System.currentTimeMillis()));

            BookSearch createdSearch = searchDAO.create(newSearch);
            return new ResponseEntity<>(createdSearch, HttpStatus.CREATED);
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create the book search", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookSearchById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookSearchDAO searchDAO = new BookSearchDAO(connection);
            BookSearch search = searchDAO.findById(id);
            if (search != null) {
                return new ResponseEntity<>(search, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Book search not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving book search", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBookSearch(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            BookSearchDAO searchDAO = new BookSearchDAO(connection);

            BookSearch updatedSearch = new BookSearch();
            updatedSearch.setSearchId(id);
            updatedSearch.setUserId(Long.parseLong(inputMap.get("userId").toString()));
            updatedSearch.setSearchQuery((String) inputMap.get("searchQuery"));
            updatedSearch.setSearchDate(new java.sql.Timestamp(System.currentTimeMillis()));

            BookSearch search = searchDAO.update(updatedSearch);
            return new ResponseEntity<>(search, HttpStatus.OK);
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the book search", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countBookSearches() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM book_searches");
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return new ResponseEntity<>("Total book searches: " + count, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to count book searches", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving book search count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookSearch(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookSearchDAO searchDAO = new BookSearchDAO(connection);
            searchDAO.delete(id);
            return new ResponseEntity<>("Book search with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete the book search", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
