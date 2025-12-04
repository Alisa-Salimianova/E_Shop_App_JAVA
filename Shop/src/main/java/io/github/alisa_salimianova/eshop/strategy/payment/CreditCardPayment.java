package io.github.alisa_salimianova.eshop.strategy.payment;

import io.github.alisa_salimianova.eshop.service.interfaces.PaymentStrategy;

public class CreditCardPayment implements PaymentStrategy {

    private static final double MIN_PAYMENT_AMOUNT = 1.0; // Избегание магических чисел
    private static final double MAX_PAYMENT_AMOUNT = 10000.0;

    @Override
    public boolean processPayment(double amount) {
        if (amount < MIN_PAYMENT_AMOUNT || amount > MAX_PAYMENT_AMOUNT) {
            System.out.printf("Сумма оплаты должна быть от $%.2f до $%.2f%n",
                    MIN_PAYMENT_AMOUNT, MAX_PAYMENT_AMOUNT);
            return false;
        }

        // Имитация обработки платежа
        System.out.printf("Обработка платежа через кредитную карту на сумму $%.2f%n", amount);
        System.out.println("Платеж успешно обработан!");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Кредитная карта";
    }
}