package com.thebookcooper.util;

public class PaymentRequestDTO {

    private Long amount; // in cents
    private String paymentMethodId;


    public PaymentRequestDTO(Long amount, String paymentMethodId) {
        this.amount = amount;
        this.paymentMethodId = paymentMethodId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}

