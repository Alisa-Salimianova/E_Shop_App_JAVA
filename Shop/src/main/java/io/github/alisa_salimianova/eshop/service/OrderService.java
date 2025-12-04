package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.dto.request.CreateOrderRequest;
import io.github.alisa_salimianova.eshop.dto.response.OrderResponse;
import io.github.alisa_salimianova.eshop.exception.InsufficientStockException;
import io.github.alisa_salimianova.eshop.exception.ResourceNotFoundException;
import io.github.alisa_salimianova.eshop.mapper.OrderMapper;
import io.github.alisa_salimianova.eshop.model.entity.Order;
import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.entity.User;
import io.github.alisa_salimianova.eshop.model.enums.OrderStatus;
import io.github.alisa_salimianova.eshop.repository.OrderRepository;
import io.github.alisa_salimianova.eshop.repository.ProductRepository;
import io.github.alisa_salimianova.eshop.repository.UserRepository;
import io.github.alisa_salimianova.eshop.service.interfaces.DeliveryStrategy;
import io.github.alisa_salimianova.eshop.service.interfaces.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request,
                                     PaymentStrategy paymentStrategy,
                                     DeliveryStrategy deliveryStrategy) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Extract product IDs and quantities
        Map<Long, Integer> productQuantities = request.getItems().stream()
                .collect(Collectors.toMap(
                        CreateOrderRequest.OrderItem::getProductId,
                        CreateOrderRequest.OrderItem::getQuantity
                ));

        List<Product> products = productRepository.findAllById(productQuantities.keySet());

        if (products.size() != productQuantities.size()) {
            throw new ResourceNotFoundException("Some products not found");
        }

        // Check stock availability
        products.forEach(product -> {
            Integer requestedQuantity = productQuantities.get(product.getId());
            if (product.getStockQuantity() < requestedQuantity) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product: %s. Available: %d, Requested: %d",
                                product.getName(), product.getStockQuantity(), requestedQuantity)
                );
            }
        });

        // Calculate totals
        BigDecimal itemsTotal = products.stream()
                .map(product -> {
                    Integer quantity = productQuantities.get(product.getId());
                    return product.getPrice().multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal deliveryCost = BigDecimal.valueOf(deliveryStrategy.calculateDeliveryCost(itemsTotal.doubleValue()));
        BigDecimal discountAmount = calculateDiscount(itemsTotal, user);
        BigDecimal finalAmount = itemsTotal.add(deliveryCost).subtract(discountAmount);

        // Process payment
        if (!paymentStrategy.processPayment(finalAmount.doubleValue())) {
            throw new RuntimeException("Payment processing failed");
        }

        // Create order
        Order order = Order.builder()
                .user(user)
                .totalAmount(itemsTotal)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .shippingAddress(request.getShippingAddress())
                .paymentMethod(paymentStrategy.getPaymentMethodName())
                .deliveryMethod(deliveryStrategy.getDeliveryMethodName())
                .build();

        // Add order items
        products.forEach(product -> {
            Integer quantity = productQuantities.get(product.getId());
            Order.OrderItem item = Order.OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            order.getItems().add(item);

            // Update stock
            product.reduceStock(quantity);
        });

        Order savedOrder = orderRepository.save(order);
        productRepository.saveAll(products);

        log.info("Order created successfully: {} for user: {}",
                savedOrder.getId(), user.getEmail());

        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new SecurityException("You are not authorized to cancel this order");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }

        // Restore stock
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.increaseStock(item.getQuantity());
            productRepository.save(product);
        });

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled: {}", orderId);
    }

    private BigDecimal calculateDiscount(BigDecimal amount, User user) {
        // Simple discount logic
        long orderCount = orderRepository.countByUser(user);

        if (orderCount >= 10) {
            return amount.multiply(BigDecimal.valueOf(0.10)); // 10% discount
        } else if (orderCount >= 5) {
            return amount.multiply(BigDecimal.valueOf(0.05)); // 5% discount
        }

        return BigDecimal.ZERO;
    }
}