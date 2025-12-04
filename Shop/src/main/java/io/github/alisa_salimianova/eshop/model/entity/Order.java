package io.github.alisa_salimianova.eshop.model.entity;

import io.github.alisa_salimianova.eshop.model.enums.OrderStatus;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PROCESSING;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();

    private LocalDateTime deliveryDate;
    private String shippingAddress;
    private String paymentMethod;
    private String deliveryMethod;

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        if (finalAmount == null) {
            finalAmount = totalAmount.subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        }
    }

    @Entity
    @Table(name = "order_items")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @Column(nullable = false)
        private Integer quantity;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal unitPrice;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal subtotal;

        @PrePersist
        @PreUpdate
        protected void calculateSubtotal() {
            if (unitPrice != null && quantity != null) {
                subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            }
        }
    }
}