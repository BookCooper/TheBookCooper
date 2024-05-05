package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thebookcooper.model.Book;
import com.thebookcooper.model.User;
import com.thebookcooper.model.Listing;
import com.thebookcooper.dao.ListingsDAO;
import com.thebookcooper.dao.BookInfoDAO;
import com.thebookcooper.dao.UserDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@CrossOrigin
@RequestMapping("/listings")
public class ListingsController {
    
    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");
    
    private Listing findListingById(long id) throws SQLException {
        try (Connection connection = dcm.getConnection()) {
            ListingsDAO listingDAO = new ListingsDAO(connection);
            return listingDAO.findById(id);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countListings() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM book_listings")) {
            if (resultSet.next()) {
                return new ResponseEntity<>("Number of listings: " + resultSet.getInt(1), HttpStatus.OK);
            }
            return new ResponseEntity<>("No listings found", HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving listing count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getListingById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {

            Listing listing = findListingById(id);

            if (listing != null) {
                return new ResponseEntity<>(listing, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Listing not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving listing", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchListingsByBookTitle(@RequestParam String title) {
        try (Connection connection = dcm.getConnection()) {

            BookInfoDAO bookInfoDAO = new BookInfoDAO(connection);
            ListingsDAO listingsDAO = new ListingsDAO(connection);
            
            // Find books by title
            List<Book> books = bookInfoDAO.findByTitle(title);
            if (books.isEmpty()) {
                return new ResponseEntity<>("No books found matching the title", HttpStatus.NOT_FOUND);
            }
            
            // For each book, find its listings
            List<Listing> listings = new ArrayList<>();
            for (Book book : books) {
                List<Listing> bookListings = listingsDAO.findByBookId(book.getBookId());
                listings.addAll(bookListings);
            }
            
            if (listings.isEmpty()) {
                return new ResponseEntity<>("No listings found for books matching the title", HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(listings, HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error searching listings by book title", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> searchListingsByBookTitle(@RequestParam long userId) {
        try (Connection connection = dcm.getConnection()) {

            ListingsDAO listingsDAO = new ListingsDAO(connection);
            
            // For each book, find its listings
            List<Listing> userListings = listingsDAO.findByUserId(userId);
            
            if (userListings.isEmpty()) {
                return new ResponseEntity<>("No listings found for this user", HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(userListings, HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error searching listings by user ID", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createListing(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Listing newListing = new Listing();
            Connection connection = dcm.getConnection();
            ListingsDAO listDAO = new ListingsDAO(connection);

            newListing.setUserId(Long.parseLong(inputMap.get("userId").toString()));
            newListing.setBookId(Long.parseLong(inputMap.get("bookId").toString()));
            newListing.setListingStatus((String) inputMap.get("listingStatus"));
            newListing.setBookCondition((String) inputMap.get("bookCondition"));
            newListing.setPrice(Double.parseDouble(inputMap.get("price").toString()));
            newListing.setListingDate(new Timestamp(System.currentTimeMillis()));
            
            Listing createdListing = listDAO.create(newListing);
            return new ResponseEntity<>(createdListing, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create the listing", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format for userID or bookID", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateListing(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            ListingsDAO listDAO = new ListingsDAO(connection);
            Listing updatedListing = findListingById(id);

            updatedListing.setUserId(Long.parseLong(inputMap.get("userId").toString()));
            updatedListing.setBookId(Long.parseLong(inputMap.get("bookId").toString()));
            updatedListing.setListingStatus((String) inputMap.get("listingStatus"));
            updatedListing.setBookCondition((String) inputMap.get("bookCondition"));
            updatedListing.setPrice(Double.parseDouble(inputMap.get("price").toString()));
            updatedListing.setListingDate(new Timestamp(System.currentTimeMillis()));

            Listing updated = listDAO.update(updatedListing);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the listing", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format for userID or bookID", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            ListingsDAO listDAO = new ListingsDAO(connection);
            listDAO.delete(id);
            return new ResponseEntity<>("Listing with id " + id + " has been deleted", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting listing with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
