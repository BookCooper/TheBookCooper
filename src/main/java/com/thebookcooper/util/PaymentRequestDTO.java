package com.thebookcooper.util;

public class PaymentRequestDTO {

    private int amount; // in cents
    private String paymentMethodId;

    public PaymentRequestDTO() {}

    public PaymentRequestDTO(int amount, String paymentMethodId) {
        this.amount = amount;
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}

