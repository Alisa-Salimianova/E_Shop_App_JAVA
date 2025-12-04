package io.github.alisa_salimianova.eshop.strategy.filter;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.service.interfaces.FilterStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryFilterStrategy implements FilterStrategy {

    @Override
    public List<Product> filter(List<Product> products, String criteria) {
        try {
            Category category = Category.valueOf(criteria.toUpperCase());
            return products.stream()
                    .filter(product -> product.getCategory() == category)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверная категория: " + criteria +
                    ". Доступные категории: " + getAvailableCategories());
        }
    }

    private String getAvailableCategories() {
        StringBuilder sb = new StringBuilder();
        for (Category category : Category.values()) {
            sb.append(category.name()).append(" (").append(category.getDisplayName()).append("), ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}