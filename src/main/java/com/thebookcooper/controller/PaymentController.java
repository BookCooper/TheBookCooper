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

import java.util.Map;


@RestController
@CrossOrigin
public class PaymentController {


    @PostMapping("/payment-request")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody String json) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
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
                return ResponseEntity.ok(new PaymentResponseDTO(true, "Payment successful"));
            } else {
                return ResponseEntity.ok(new PaymentResponseDTO(false, "Payment failed with status: " + paymentIntent.getStatus()));
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new PaymentResponseDTO(false, "Error processing payment: " + e.getMessage()));
        }catch (JsonProcessingException e) {
            System.err.println("Error converting payment request to JSON string: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentResponseDTO(false, "Error logging payment request"));
        }
    }
}
