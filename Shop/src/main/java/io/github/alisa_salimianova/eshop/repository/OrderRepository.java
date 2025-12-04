package io.github.alisa_salimianova.eshop.repository;

import io.github.alisa_salimianova.eshop.model.entity.Order;
import io.github.alisa_salimianova.eshop.model.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderRepository {
    private final List<Order> orders;
    private final AtomicInteger idCounter;

    public OrderRepository() {
        this.orders = new ArrayList<>();
        this.idCounter = new AtomicInteger(1);
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    public Optional<Order> findById(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst();
    }

    public List<Order> findByUserId(int userId) {
        return orders.stream()
                .filter(order -> order.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

    public void save(Order order) {
        orders.add(order);
    }

    public int getNextId() {
        return idCounter.getAndIncrement();
    }
}