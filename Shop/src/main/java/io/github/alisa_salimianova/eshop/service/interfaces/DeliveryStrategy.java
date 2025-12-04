package io.github.alisa_salimianova.eshop.service.interfaces;

public interface DeliveryStrategy {
    double calculateDeliveryCost(double orderAmount);
    int getDeliveryDays();
    String getDeliveryMethodName();
}