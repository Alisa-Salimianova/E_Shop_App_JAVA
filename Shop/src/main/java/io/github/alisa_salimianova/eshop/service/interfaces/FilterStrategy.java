package io.github.alisa_salimianova.eshop.service.interfaces;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import java.util.List;

public interface FilterStrategy {
    List<Product> filter(List<Product> products, String criteria);
}