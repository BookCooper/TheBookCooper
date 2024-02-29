package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.thebookcooper.model.BookTransaction;
import com.thebookcooper.dao.BookTransactionDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@RequestMapping("/transactions") // Base path for transactions
public class BookTransactionController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookTransactionDAO transactionDAO = new BookTransactionDAO(connection);
            BookTransaction transaction = transactionDAO.findById(id);
            if (transaction != null) {
                return new ResponseEntity<>(transaction, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countTransactions() {
        try (Connection connection = dcm.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM book_transactions")) {
            if (resultSet.next()) {
                return new ResponseEntity<>("Number of transactions: " + resultSet.getInt(1), HttpStatus.OK);
            }
            return new ResponseEntity<>("No transactions found", HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving transaction count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            BookTransaction newTransaction = new BookTransaction();
            Connection connection = dcm.getConnection();
            BookTransactionDAO transactionDAO = new BookTransactionDAO(connection);

            newTransaction.setBuyerId(Long.parseLong(inputMap.get("buyerId").toString()));
            newTransaction.setSellerId(Long.parseLong(inputMap.get("sellerId").toString()));
            newTransaction.setListingId(Long.parseLong(inputMap.get("listingId").toString()));
            newTransaction.setTransactionPrice(Double.parseDouble(inputMap.get("transactionPrice").toString()));
            newTransaction.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
            newTransaction.setTransactionStatus((String) inputMap.get("transactionStatus"));

            BookTransaction createdTransaction = transactionDAO.create(newTransaction);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create the transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            BookTransactionDAO transactionDAO = new BookTransactionDAO(connection);

            BookTransaction updatedTransaction = new BookTransaction();
            updatedTransaction.setTransactionId(id);
            updatedTransaction.setBuyerId(Long.parseLong(inputMap.get("buyerId").toString()));
            updatedTransaction.setSellerId(Long.parseLong(inputMap.get("sellerId").toString()));
            updatedTransaction.setListingId(Long.parseLong(inputMap.get("listingId").toString()));
            updatedTransaction.setTransactionPrice(Double.parseDouble(inputMap.get("transactionPrice").toString()));
            updatedTransaction.setTransactionStatus((String) inputMap.get("transactionStatus"));

            BookTransaction transaction = transactionDAO.update(updatedTransaction);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            BookTransactionDAO transactionDAO = new BookTransactionDAO(connection);
            transactionDAO.delete(id);
            return new ResponseEntity<>("Transaction with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete the transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
