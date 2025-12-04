package io.github.alisa_salimianova.eshop.model.enums;

/**
 * Статусы заказа.
 * Избегание магических чисел: статусы определены как константы.
 */
public enum OrderStatus {
    PROCESSING("В обработке"),
    CONFIRMED("Подтвержден"),
    SHIPPED("Отправлен"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменен");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}