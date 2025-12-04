package io.github.alisa_salimianova.eshop.strategy.payment;

import io.github.alisa_salimianova.eshop.service.interfaces.PaymentStrategy;

public class PayPalPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        if (amount <= 0) {
            System.out.println("Сумма оплаты должна быть положительной");
            return false;
        }

        // Имитация обработки платежа
        System.out.printf("Обработка платежа через PayPal на сумму $%.2f%n", amount);
        System.out.println("Перенаправление на сайт PayPal...");
        System.out.println("Платеж успешно обработан!");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "PayPal";
    }
}