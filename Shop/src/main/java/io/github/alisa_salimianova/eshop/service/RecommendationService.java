package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.dto.response.ProductResponse;
import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final ProductService productService;
    private final OrderService orderService;

    public List<ProductResponse> getRecommendationsForUser(Long userId) {
        log.info("Generating recommendations for user: {}", userId);

        // Get user's order history
        var userOrders = orderService.getUserOrders(userId);

        // Extract purchased product categories
        Set<Category> purchasedCategories = userOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(item -> {
                    // This would need a method to get product category from productId
                    // For now, we'll use a placeholder
                    return Category.ELECTRONICS; // Should be actual product category
                })
                .collect(Collectors.toSet());

        // Get products from purchased categories
        List<ProductResponse> recommendations = new ArrayList<>();
        for (Category category : purchasedCategories) {
            // We need to add pagination support to ProductService
            // recommendations.addAll(productService.getProductsByCategory(category, Pageable.unpaged()));
        }

        // Limit recommendations
        return recommendations.stream()
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getTopRatedProducts() {
        log.info("Getting top rated products");

        // We need to add this method to ProductRepository
        // For now, we'll use a placeholder
        return Collections.emptyList();
    }
}