package io.github.alisa_salimianova.eshop.model.entity;

import io.github.alisa_salimianova.eshop.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Применение инвариантов: проверка состояния при изменении статуса.
 */
public class Order {
    private final int id;
    private final User user;
    private final List<Product> products;
    private final LocalDateTime orderDate;
    private OrderStatus status;
    private final double totalAmount;

    public Order(int id, User user, List<Product> products) {
        this.id = id;
        this.user = user;
        this.products = new ArrayList<>(products);
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PROCESSING;
        this.totalAmount = calculateTotalAmount(products);
    }

    // Геттеры
    public int getId() { return id; }
    public User getUser() { return user; }
    public List<Product> getProducts() { return new ArrayList<>(products); }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }

    public void setStatus(OrderStatus status) {
        // Запрещаем менять статус доставленного заказа
        if (this.status == OrderStatus.DELIVERED && status != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Нельзя изменить статус доставленного заказа");
        }
        this.status = status;
    }

    private double calculateTotalAmount(List<Product> products) {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    @Override
    public String toString() {
        return String.format("Заказ #%d - %s - $%.2f (%s)",
                id, status.getDisplayName(), totalAmount, orderDate);
    }
}