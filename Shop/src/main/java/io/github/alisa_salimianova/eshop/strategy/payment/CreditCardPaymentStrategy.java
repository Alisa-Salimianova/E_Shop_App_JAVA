package io.github.alisa_salimianova.eshop.strategy.payment;

import io.github.alisa_salimianova.eshop.service.interfaces.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditCardPaymentStrategy implements PaymentStrategy {

    private static final double MIN_PAYMENT_AMOUNT = 1.0;
    private static final double MAX_PAYMENT_AMOUNT = 10000.0;

    @Override
    public boolean processPayment(double amount) {
        if (amount < MIN_PAYMENT_AMOUNT || amount > MAX_PAYMENT_AMOUNT) {
            log.error("Payment amount {} is out of range", amount);
            return false;
        }

        log.info("Processing credit card payment for amount: {}", amount);
        // Simulate payment processing
        try {
            Thread.sleep(100); // Simulate processing time
            log.info("Credit card payment processed successfully");
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Payment processing interrupted", e);
            return false;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }
}