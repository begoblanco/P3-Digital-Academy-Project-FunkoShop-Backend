package org.factoriaf5.digital_academy.funko_shop.stripe.stripe_exceptions;

public class PaymentRequestInvalidException extends RuntimeException {
    public PaymentRequestInvalidException(String message) {
        super(message);
    }

    public PaymentRequestInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}

