package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.thebookcooper.model.PointTransaction;
import com.thebookcooper.dao.PointTransactionDAO;
import com.thebookcooper.dao.UserDAO;
import com.thebookcooper.model.User;
import com.thebookcooper.dao.DatabaseConnectionManager;

@RestController
@RequestMapping("/point-transactions") // Base path for point transactions
public class PointTransactionController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");

    @GetMapping("/count")
    public ResponseEntity<?> countPointTransactions() {
        try (Connection connection = dcm.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM point_transactions")) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return new ResponseEntity<>("Number of point transactions: " + count, HttpStatus.OK);
            }
            return new ResponseEntity<>("No point transactions found", HttpStatus.NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving point transaction count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPointTransactionById(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            PointTransactionDAO transactionDAO = new PointTransactionDAO(connection);
            PointTransaction transaction = transactionDAO.findById(id);
            if (transaction != null) {
                return new ResponseEntity<>(transaction, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Point transaction not found", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving point transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //should probably reference store-item to buy the B-Buck package

    @PostMapping("/create")
    public ResponseEntity<?> createPointTransaction(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            PointTransactionDAO transactionDAO = new PointTransactionDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
    
            long userId = Long.parseLong(inputMap.get("userId").toString());
            String transactionType = (String) inputMap.get("transactionType");
            java.math.BigDecimal amount = new java.math.BigDecimal(inputMap.get("amount").toString());
    
            // Fetch the latest balance for the user
            User user = userDAO.findById(userId);
            java.math.BigDecimal currentBalance = java.math.BigDecimal.valueOf(user.getBBucksBalance());
    
            // Calculate the new balance based on the transaction type
            if ("Purchase".equals(transactionType)) {
                currentBalance = currentBalance.subtract(amount);
            } else if ("Deposit".equals(transactionType)) {
                currentBalance = currentBalance.add(amount);
            }
    
            // Update the user's balance in the User table
            user.setBBucksBalance(currentBalance.doubleValue());
            userDAO.update(user);
    
            // Create the point transaction with the updated balance
            PointTransaction newTransaction = new PointTransaction();
            newTransaction.setUserId(userId);
            newTransaction.setTransactionType(transactionType);
            newTransaction.setAmount(amount);
            newTransaction.setCurrentBalance(currentBalance);
            newTransaction.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
    
            PointTransaction createdTransaction = transactionDAO.create(newTransaction);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create the point transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePointTransaction(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Connection connection = dcm.getConnection();
            PointTransactionDAO transactionDAO = new PointTransactionDAO(connection);

            PointTransaction transactionToUpdate = new PointTransaction();
            transactionToUpdate.setBbTransactionId(id);
            transactionToUpdate.setUserId(Long.parseLong(inputMap.get("userId").toString()));
            transactionToUpdate.setTransactionType((String) inputMap.get("transactionType"));
            transactionToUpdate.setAmount(new java.math.BigDecimal(inputMap.get("amount").toString()));
            transactionToUpdate.setCurrentBalance(new java.math.BigDecimal(inputMap.get("currentBalance").toString()));

            PointTransaction updatedTransaction = transactionDAO.update(transactionToUpdate);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON input", HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the point transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePointTransaction(@PathVariable("id") long id) {
        try (Connection connection = dcm.getConnection()) {
            PointTransactionDAO transactionDAO = new PointTransactionDAO(connection);
            transactionDAO.delete(id);
            return new ResponseEntity<>("Point transaction with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to delete the point transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
