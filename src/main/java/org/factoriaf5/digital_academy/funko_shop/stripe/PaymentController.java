package org.factoriaf5.digital_academy.funko_shop.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.factoriaf5.digital_academy.funko_shop.stripe.stripe_exceptions.PaymentFailedException;
import org.factoriaf5.digital_academy.funko_shop.stripe.stripe_exceptions.PaymentRequestInvalidException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-endpoint}/payments")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class PaymentController {

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeApiKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        Stripe.apiKey = stripeApiKey;

        if (paymentRequest.getAmount() == null || paymentRequest.getAmount() <= 0) {
            throw new PaymentRequestInvalidException("El monto del pago debe ser mayor a cero.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentRequest.getAmount());
        params.put("currency", "eur");
        params.put("automatic_payment_methods[enabled]", true);

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", intent.getClientSecret());
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            throw new PaymentFailedException("No se pudo procesar el pago, inténtelo nuevamente más tarde.", e);
        }
    }
}
