package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class RecommendationService {
    private final ProductService productService;
    private final OrderService orderService;

    public RecommendationService(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    public List<Product> getRecommendationsForUser(int userId) {
        List<Order> userOrders = orderService.getUserOrders(userId);

        return userOrders.stream()
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .flatMap(product ->
                        productService.getProductsByCategory(product.getCategory()).stream())
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Product> getTopRatedProducts() {
        return productService.getAllProducts().stream()
                .sorted((p1, p2) -> Double.compare(p2.getRating(), p1.getRating()))
                .limit(3)
                .collect(Collectors.toList());
    }
}