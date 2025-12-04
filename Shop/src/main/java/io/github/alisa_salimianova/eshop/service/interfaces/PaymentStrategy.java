package io.github.alisa_salimianova.eshop.service.interfaces;

public interface PaymentStrategy {
    boolean processPayment(double amount);
    String getPaymentMethodName();
}