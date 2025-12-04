package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.model.entity.Order;
import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.entity.User;
import io.github.alisa_salimianova.eshop.model.enums.OrderStatus;
import io.github.alisa_salimianova.eshop.repository.OrderRepository;
import io.github.alisa_salimianova.eshop.repository.UserRepository;
import io.github.alisa_salimianova.eshop.service.interfaces.PaymentStrategy;
import io.github.alisa_salimianova.eshop.service.interfaces.DeliveryStrategy;

import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public Order createOrder(int userId, PaymentStrategy paymentStrategy, DeliveryStrategy deliveryStrategy) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        List<Product> cart = user.getCart();
        if (cart.isEmpty()) {
            throw new IllegalStateException("Корзина пуста");
        }

        double orderAmount = user.getCartTotal();
        double deliveryCost = deliveryStrategy.calculateDeliveryCost(orderAmount);
        double totalAmount = orderAmount + deliveryCost;

        // Обработка платежа
        if (!paymentStrategy.processPayment(totalAmount)) {
            throw new IllegalStateException("Ошибка обработки платежа");
        }

        // Создание заказа
        Order order = new Order(orderRepository.getNextId(), user, cart);
        orderRepository.save(order);

        // Очистка корзины и добавление в историю заказов
        user.clearCart();
        user.addOrder(order);

        System.out.printf("Заказ создан! Номер: #%d, Сумма: $%.2f (доставка: $%.2f)%n",
                order.getId(), orderAmount, deliveryCost);
        System.out.printf("Способ доставки: %s (%d дней)%n",
                deliveryStrategy.getDeliveryMethodName(), deliveryStrategy.getDeliveryDays());

        return order;
    }

    public Order repeatOrder(int orderId, int userId) {
        Order originalOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (originalOrder.getUser().getId() != userId) {
            throw new IllegalArgumentException("Это не ваш заказ");
        }

        User user = userRepository.findById(userId).get();
        List<Product> products = originalOrder.getProducts();

        // Добавляем товары в корзину
        products.forEach(user::addToCart);

        return originalOrder;
    }

    public void cancelOrder(int orderId, int userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (order.getUser().getId() != userId) {
            throw new IllegalArgumentException("Это не ваш заказ");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Нельзя отменить доставленный заказ");
        }

        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Заказ #" + orderId + " отменен");
    }

    public List<Order> getUserOrders(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        order.setStatus(status);
    }
}