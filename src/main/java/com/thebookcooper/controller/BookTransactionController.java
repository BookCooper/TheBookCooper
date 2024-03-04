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
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

import com.thebookcooper.model.BookTransaction;
import com.thebookcooper.model.PointTransaction;
import com.thebookcooper.model.User;
import com.thebookcooper.dao.BookTransactionDAO;
import com.thebookcooper.dao.DatabaseConnectionManager;
import com.thebookcooper.dao.PointTransactionDAO;
import com.thebookcooper.dao.UserDAO;

@RestController
@RequestMapping("/book-transactions") // Base path for transactions
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
        ResponseEntity<?> responseEntity;
        try (Connection connection = dcm.getConnection()) {
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            
            BookTransactionDAO transactionDAO = new BookTransactionDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            PointTransactionDAO pointTransactionDAO = new PointTransactionDAO(connection);
            

            // we don't necessairly need transaction price; it's in the listing
            long buyerId = Long.parseLong(inputMap.get("buyerId").toString());
            double transactionPrice = Double.parseDouble(inputMap.get("transactionPrice").toString());
            String transactionStatus = (String) inputMap.get("transactionStatus");

            // Create the book transaction 
            BookTransaction newTransaction = new BookTransaction();
            newTransaction.setBuyerId(buyerId);
            newTransaction.setSellerId(Long.parseLong(inputMap.get("sellerId").toString()));
            newTransaction.setListingId(Long.parseLong(inputMap.get("listingId").toString()));
            newTransaction.setTransactionPrice(transactionPrice);
            newTransaction.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
            newTransaction.setTransactionStatus(transactionStatus);
            BookTransaction createdTransaction = transactionDAO.create(newTransaction);

            // Get buyer's current balance
            User buyer = userDAO.findById(buyerId);
            java.math.BigDecimal currentBalance = pointTransactionDAO.fetchCurrentBalanceByUserId(buyerId);
            
            String trans_string = String.valueOf(transactionPrice);
            

            double curBal = Double.parseDouble(trans_string);
            //Double.parseDouble(inputMap.get("price").toString()))
            //
            double newnewBalance;
            
            if(currentBalance.doubleValue() >= transactionPrice) {
                newnewBalance = currentBalance.doubleValue() - transactionPrice;
            }
            else {
                responseEntity = new ResponseEntity<>("Insufficient Balance. Failed to process the transaction.", HttpStatus.INTERNAL_SERVER_ERROR);
                return responseEntity; 
            }
            
            String newnew_string = String.valueOf(newnewBalance);

            java.math.BigDecimal updatedBalance = new java.math.BigDecimal(newnew_string);
            
            //updatedBalance = java.math.BigDecimal(updatedBalance)

            String pointTransactionType = "";
            if ("purchase completed".equalsIgnoreCase(transactionStatus)) {
                updatedBalance = currentBalance.subtract(java.math.BigDecimal.valueOf(transactionPrice));
                pointTransactionType = "Purchase Completed";
            } else if ("purchase pending".equalsIgnoreCase(transactionStatus)) {
                updatedBalance = currentBalance.subtract(java.math.BigDecimal.valueOf(transactionPrice));
                pointTransactionType = "Purchase Pending";
            }

            // Update the buyer's balance 
            buyer.setBBucksBalance(newnewBalance);
            userDAO.update(buyer);

            // Create a new point transaction 
            PointTransaction pointTransaction = new PointTransaction();
            pointTransaction.setUserId(buyerId);
            pointTransaction.setTransactionType(pointTransactionType);
            pointTransaction.setAmount(java.math.BigDecimal.valueOf(transactionPrice));
            pointTransaction.setCurrentBalance(updatedBalance);
            pointTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
            PointTransaction createdPointTransaction = pointTransactionDAO.create(pointTransaction);

            // Return the transactions and the user
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("bookTransaction", createdTransaction);
            responseBody.put("user", buyer);
            responseBody.put("pointTransaction", createdPointTransaction);
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (JsonProcessingException | SQLException | NumberFormatException e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity<>("Failed to process the transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable("id") long id, @RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            //want to get the transaction of the specific id rather than just overwrite it

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
            updatedTransaction.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));

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
