package io.github.alisa_salimianova.eshop.model.entity;

import io.github.alisa_salimianova.eshop.model.enums.Category;

/**
 * Применение SRP: класс отвечает только за данные товара.
 */
public class Product {
    private final int id;
    private final String name;
    private final String description;
    private final double price;
    private final Category category;
    private final String manufacturer;
    private double rating;
    private int ratingCount;

    public Product(int id, String name, String description, double price,
                   Category category, String manufacturer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.manufacturer = manufacturer;
        this.rating = 0.0;
        this.ratingCount = 0;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }
    public String getManufacturer() { return manufacturer; }
    public double getRating() { return rating; }
    public int getRatingCount() { return ratingCount; }

    /**
     * Добавление оценки товару.
     * Применение инкапсуляции: логика расчета рейтинга скрыта внутри класса.
     */
    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5");
        }
        double totalRating = this.rating * this.ratingCount + rating;
        this.ratingCount++;
        this.rating = totalRating / this.ratingCount;
    }

    @Override
    public String toString() {
        return String.format("%d. %s - $%.2f (Рейтинг: %.1f⭐, Категория: %s)",
                id, name, price, rating, category.getDisplayName());
    }
}
