package io.github.alisa_salimianova.eshop.strategy.delivery;

import io.github.alisa_salimianova.eshop.service.interfaces.DeliveryStrategy;

/**
 * Стратегия экспресс-доставки.
 * Применение LSP: может использоваться везде, где требуется DeliveryStrategy.
 */
public class ExpressDeliveryStrategy implements DeliveryStrategy {

    private static final double EXPRESS_DELIVERY_COST = 10.0;
    private static final int EXPRESS_DELIVERY_DAYS = 1;

    @Override
    public double calculateDeliveryCost(double orderAmount) {
        return EXPRESS_DELIVERY_COST;
    }

    @Override
    public int getDeliveryDays() {
        return EXPRESS_DELIVERY_DAYS;
    }

    @Override
    public String getDeliveryMethodName() {
        return "Экспресс-доставка";
    }
}