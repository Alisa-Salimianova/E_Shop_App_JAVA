package io.github.alisa_salimianova.eshop.strategy.filter;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.service.interfaces.FilterStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PriceFilterStrategy implements FilterStrategy {

    private static final double MAX_PRICE_LIMIT = 10000.0;

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

        BigDecimal maxPriceDecimal = BigDecimal.valueOf(maxPrice);

        return products.stream()
                .filter(product -> product.getPrice().compareTo(maxPriceDecimal) <= 0)
                .collect(Collectors.toList());
    }
}