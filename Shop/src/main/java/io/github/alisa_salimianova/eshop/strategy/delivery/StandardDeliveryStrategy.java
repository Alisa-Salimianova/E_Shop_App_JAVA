package io.github.alisa_salimianova.eshop.strategy.delivery;

import io.github.alisa_salimianova.eshop.service.interfaces.DeliveryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StandardDeliveryStrategy implements DeliveryStrategy {

    private static final double DELIVERY_COST_PERCENTAGE = 0.05; // 5%
    private static final double MIN_DELIVERY_COST = 2.0;
    private static final int STANDARD_DELIVERY_DAYS = 5;

    @Override
    public double calculateDeliveryCost(double orderAmount) {
        double cost = orderAmount * DELIVERY_COST_PERCENTAGE;
        double finalCost = Math.max(MIN_DELIVERY_COST, cost);
        log.info("Calculated standard delivery cost: {} for order amount: {}", finalCost, orderAmount);
        return finalCost;
    }

    @Override
    public int getDeliveryDays() {
        return STANDARD_DELIVERY_DAYS;
    }

    @Override
    public String getDeliveryMethodName() {
        return "Standard Delivery";
    }
}