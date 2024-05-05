package com.thebookcooper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.thebookcooper.util.PaymentRequestDTO;
import com.thebookcooper.util.PaymentResponseDTO;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import com.thebookcooper.model.PointTransaction;
import com.thebookcooper.dao.PointTransactionDAO;
import com.thebookcooper.model.User;
import com.thebookcooper.dao.UserDAO;

import java.util.Map;
import com.thebookcooper.dao.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Timestamp;


@RestController
@CrossOrigin
public class PaymentController {

    private final DatabaseConnectionManager dcm = new DatabaseConnectionManager("db", 5432, "thebookcooper", "BCdev", "password");
    
    @PostMapping("/payment-request")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody String json) {

        ObjectMapper objectMapper = new ObjectMapper();

        try (Connection connection = dcm.getConnection()){
            Map<String, Object> inputMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            //System.out.println("Received payment request: " + json);

            PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
            paymentRequest.setAmount((int) inputMap.get("amount"));
            paymentRequest.setPaymentMethodId((String) inputMap.get("id"));

            PaymentIntentCreateParams.Builder builder = PaymentIntentCreateParams.builder()
                .setAmount((long) paymentRequest.getAmount())
                .setCurrency("usd")
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC);

            if (paymentRequest.getPaymentMethodId() != null && !paymentRequest.getPaymentMethodId().isEmpty()) {
                builder.setPaymentMethod((String) paymentRequest.getPaymentMethodId());
                builder.setConfirm(true);
                builder.setReturnUrl("http://localhost:3000/payment-success");
            }

            PaymentIntentCreateParams params = builder.build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if ("succeeded".equals(paymentIntent.getStatus())) {

                // Get buyer's current balance
                UserDAO userDAO = new UserDAO(connection);
                PointTransactionDAO pointTransactionDAO = new PointTransactionDAO(connection);

                long userId = Long.parseLong(inputMap.get("userId").toString());
                User user = userDAO.findById(userId);

                BigDecimal pointAmount = new BigDecimal(inputMap.get("pointAmount").toString());

                BigDecimal currentUserBalance = pointTransactionDAO.fetchCurrentBalanceByUserId(userId);

                BigDecimal newBal = currentUserBalance.add(pointAmount);

                user.setBBucksBalance(Double.parseDouble(newBal.toString()));
                userDAO.update(user);

                PointTransaction newTransaction = new PointTransaction();
                newTransaction.setUserId(userId);
                newTransaction.setTransactionType("Point");
                newTransaction.setAmount(pointAmount);
                newTransaction.setCurrentBalance(newBal);
                newTransaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));

                PointTransaction createdTransaction = pointTransactionDAO.create(newTransaction);

                /*
                double pointAmount = Double.parseDouble((String) inputMap.get(("pointAmount")));

                java.math.BigDecimal currentUserBalance = pointTransactionDAO.fetchCurrentBalanceByUserId(userId);
                double curBal = Double.parseDouble(currentUserBalance);

                double newBal = curBal + pointAmount;
                
                // Update the buyer's balance 
                user.setBBucksBalance(newBal);
                userDAO.update(user);

                // Create the point transaction with the updated balance
                PointTransaction newTransaction = new PointTransaction();
                newTransaction.setUserId(userId);
                newTransaction.setTransactionType("Point");
                newTransaction.setAmount(pointAmount);
                newTransaction.setCurrentBalance(newBal);
                newTransaction.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
        
                PointTransaction createdTransaction = pointTransactionDAO.create(newTransaction);// Create a new point transaction */

                return ResponseEntity.ok(new PaymentResponseDTO(true, "Payment successful"));
            } 
            else {
                return ResponseEntity.ok(new PaymentResponseDTO(false, "Payment failed with status: " + paymentIntent.getStatus()));
            }
        
        }catch (SQLException | NumberFormatException | StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new PaymentResponseDTO(false, "Error processing payment: " + e.getMessage()));
        }catch (JsonProcessingException e) {
            System.err.println("Error converting payment request to JSON string: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentResponseDTO(false, "Error logging payment request"));
        }


    }
    
}
