package io.github.alisa_salimianova.eshop.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime registeredAt = LocalDateTime.now();

    private String phone;
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Builder.Default
    private List<Product> wishlist = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_cart_items", joinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItem {
        @Column(name = "product_id")
        private Long productId;

        @Column(name = "quantity")
        @Builder.Default
        private Integer quantity = 1;
    }

    public void addToCart(Long productId, Integer quantity) {
        this.cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + quantity),
                        () -> this.cartItems.add(new CartItem(productId, quantity))
                );
    }

    public void removeFromCart(Long productId) {
        this.cartItems.removeIf(item -> item.getProductId().equals(productId));
    }

    public void clearCart() {
        this.cartItems.clear();
    }
}