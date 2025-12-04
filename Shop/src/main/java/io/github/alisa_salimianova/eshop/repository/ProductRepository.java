package io.github.alisa_salimianova.eshop.repository;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.enums.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProductRepository {
    private final List<Product> products;
    private final AtomicInteger idCounter;

    public ProductRepository() {
        this.products = new ArrayList<>();
        this.idCounter = new AtomicInteger(1);
        initializeSampleProducts();
    }

    private void initializeSampleProducts() {
        // Избегание магических чисел: константы для цен
        save(new Product(idCounter.getAndIncrement(), "iPhone 15",
                "Смартфон Apple", 999.99, Category.ELECTRONICS, "Apple"));
        save(new Product(idCounter.getAndIncrement(), "Футболка",
                "Хлопковая футболка", 19.99, Category.CLOTHING, "Nike"));
        save(new Product(idCounter.getAndIncrement(), "Java Head First",
                "Книга по Java", 39.99, Category.BOOKS, "O'Reilly"));
        save(new Product(idCounter.getAndIncrement(), "Фитнес-браслет",
                "Умный браслет", 49.99, Category.SPORTS, "Xiaomi"));
        save(new Product(idCounter.getAndIncrement(), "Кофемашина",
                "Автоматическая кофемашина", 299.99, Category.HOME, "Philips"));
        save(new Product(idCounter.getAndIncrement(), "Ноутбук",
                "Игровой ноутбук", 1299.99, Category.ELECTRONICS, "ASUS"));
        save(new Product(idCounter.getAndIncrement(), "Джинсы",
                "Классические джинсы", 59.99, Category.CLOTHING, "Levi's"));
        save(new Product(idCounter.getAndIncrement(), "Чистый код",
                "Книга Роберта Мартина", 29.99, Category.BOOKS, "Prentice Hall"));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(int id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    public List<Product> findByCategory(Category category) {
        return products.stream()
                .filter(product -> product.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void save(Product product) {
        products.add(product);
    }
}