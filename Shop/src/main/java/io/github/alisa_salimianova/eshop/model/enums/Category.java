package io.github.alisa_salimianova.eshop.model.enums;
/**
 * Категории товаров.
 * Применение DRY: вместо строк используем enum для избежания дублирования.
 */
public enum Category {
    ELECTRONICS("Электроника"),
    CLOTHING("Одежда"),
    BOOKS("Книги"),
    SPORTS("Спорт"),
    HOME("Для дома");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}