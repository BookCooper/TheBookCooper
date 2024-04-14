package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.thebookcooper.model.BookTag;
import com.thebookcooper.dao.BookTagDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@CrossOrigin
@RequestMapping("/booktags") // Base path 
public class BookTagController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookTagById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookTagDAO tagDAO = new BookTagDAO(connection);
            BookTag tag = tagDAO.findById(id);
            if (tag != null) {
                return new ResponseEntity<>(tag, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Book tag not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving book tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countBookTags() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM book_tags")) {
            if (resultSet.next()) {
                int count = resultSet.getInt("total");
                return new ResponseEntity<>("Number of book tags: " + count, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No book tags found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving book tag count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBookTag(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            BookTag newTag = new BookTag();
            Connection connection = dcm.getConnection();
            BookTagDAO tagDAO = new BookTagDAO(connection);

            newTag.setTagName((String) inputMap.get("tagName"));
            newTag.setBookId(Long.parseLong(inputMap.get("bookId").toString()));
            
            BookTag createdTag = tagDAO.create(newTag);
            return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
        } catch (JsonProcessingException | NumberFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input or bookId format", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create the book tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBookTag(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            BookTagDAO tagDAO = new BookTagDAO(connection);

            BookTag updatedTag = new BookTag();
            updatedTag.setTagId(id);
            updatedTag.setTagName((String) inputMap.get("tagName"));
            updatedTag.setBookId(Long.parseLong(inputMap.get("bookId").toString()));
            
            BookTag tag = tagDAO.update(updatedTag);
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } catch (JsonProcessingException | NumberFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input or bookId format", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the book tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBookTag(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookTagDAO tagDAO = new BookTagDAO(connection);
            tagDAO.delete(id);
            return new ResponseEntity<>("Book tag with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete the book tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
