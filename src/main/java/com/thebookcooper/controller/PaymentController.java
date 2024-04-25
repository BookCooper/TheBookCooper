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


@RestController
@CrossOrigin
public class PaymentController {

    @PostMapping("/payment-request")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        try {

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentRequest.getAmount())
                .setCurrency("usd")
                .setPaymentMethod(paymentRequest.getPaymentMethodId())
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC)
                .setConfirm(true)
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if ("succeeded".equals(paymentIntent.getStatus())) {
                return ResponseEntity.ok(new PaymentResponseDTO(true, "Payment successful"));
            } else {
                return ResponseEntity.ok(new PaymentResponseDTO(false, "Payment failed with status: " + paymentIntent.getStatus()));
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new PaymentResponseDTO(false, "Error processing payment: " + e.getMessage()));
        }
    }
}

