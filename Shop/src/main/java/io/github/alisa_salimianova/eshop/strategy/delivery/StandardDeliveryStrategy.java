package io.github.alisa_salimianova.eshop.strategy.delivery;

import io.github.alisa_salimianova.eshop.service.interfaces.DeliveryStrategy;

public class StandardDelivery implements DeliveryStrategy {

    private static final double DELIVERY_COST_PERCENTAGE = 0.05; // 5%
    private static final double MIN_DELIVERY_COST = 2.0;
    private static final int STANDARD_DELIVERY_DAYS = 5;

    @Override
    public double calculateDeliveryCost(double orderAmount) {
        double cost = orderAmount * DELIVERY_COST_PERCENTAGE;
        return Math.max(MIN_DELIVERY_COST, cost);
    }

    @Override
    public int getDeliveryDays() {
        return STANDARD_DELIVERY_DAYS;
    }

    @Override
    public String getDeliveryMethodName() {
        return "Стандартная доставка";
    }
}