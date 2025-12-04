package io.github.alisa_salimianova.eshop.model.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final int id;
    private final String name;
    private final String email;
    private final List<Product> cart;
    private final List<Order> orderHistory;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cart = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Product> getCart() { return new ArrayList<>(cart); }
    public List<Order> getOrderHistory() { return new ArrayList<>(orderHistory); }

    public void addToCart(Product product) {
        cart.add(product);
    }

    public void removeFromCart(Product product) {
        cart.remove(product);
    }

    public void clearCart() {
        cart.clear();
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }
    public double getCartTotal() {
        return cart.stream().mapToDouble(Product::getPrice).sum();
    }

    @Override
    public String toString() {
        return String.format("Пользователь: %s (Email: %s)", name, email);
    }
}