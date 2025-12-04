package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.repository.ProductRepository;
import io.github.alisa_salimianova.eshop.service.interfaces.FilterStrategy;

import java.util.List;

/**
 * Сервис для управления товарами.
 * Применение SRP: отвечает только за бизнес-логику товаров.
 * Применение DIP: зависит от абстракции FilterStrategy, а не от конкретных реализаций.
 */
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
    }

    /**
     * Фильтрация товаров по стратегии.
     * Применение DIP: метод работает с абстракцией FilterStrategy.
     */
    public List<Product> filterProducts(FilterStrategy strategy, String criteria) {
        return strategy.filter(productRepository.findAll(), criteria);
    }

    /**
     * Оценка товара.
     * Применение валидации: проверка корректности оценки.
     */
    public void rateProduct(int productId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5");
        }
        Product product = getProductById(productId);
        product.addRating(rating);
    }
}
