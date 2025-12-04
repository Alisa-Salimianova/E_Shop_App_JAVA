package io.github.alisa_salimianova.eshop.strategy.filter;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.service.interfaces.FilterStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class PriceFilterStrategy implements FilterStrategy {

    private static final double MAX_PRICE_LIMIT = 10000.0; // Избегание магических чисел

    @Override
    public List<Product> filter(List<Product> products, String criteria) {
        double maxPrice;
        try {
            maxPrice = Double.parseDouble(criteria);
            if (maxPrice < 0 || maxPrice > MAX_PRICE_LIMIT) {
                throw new IllegalArgumentException("Максимальная цена должна быть от 0 до " + MAX_PRICE_LIMIT);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат цены: " + criteria);
        }

        return products.stream()
                .filter(product -> product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
}
