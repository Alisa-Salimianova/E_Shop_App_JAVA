package io.github.alisa_salimianova.eshop.dto.response;

import io.github.alisa_salimianova.eshop.model.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private String manufacturer;
    private String sku;
    private Integer stockQuantity;
    private Double rating;
    private Integer ratingCount;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}