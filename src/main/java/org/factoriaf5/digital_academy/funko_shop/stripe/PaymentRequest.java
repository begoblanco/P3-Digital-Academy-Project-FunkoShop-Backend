package org.factoriaf5.digital_academy.funko_shop.stripe;

public class PaymentRequest {
    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}