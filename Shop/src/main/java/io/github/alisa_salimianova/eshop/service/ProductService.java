package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.dto.request.CreateProductRequest;
import io.github.alisa_salimianova.eshop.dto.request.UpdateProductRequest;
import io.github.alisa_salimianova.eshop.dto.response.ProductResponse;
import io.github.alisa_salimianova.eshop.exception.ResourceNotFoundException;
import io.github.alisa_salimianova.eshop.mapper.ProductMapper;
import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'all-active'")
    public List<ProductResponse> getAllActiveProducts() {
        log.info("Fetching all active products");
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Category category, Pageable pageable) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategory(category, pageable)
                .map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsUnderPrice(BigDecimal maxPrice) {
        log.info("Fetching products under price: {}", maxPrice);
        return productRepository.findProductsUnderPrice(maxPrice).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "products", key = "'all-active'")
    })
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        // Check if SKU already exists
        if (productRepository.findBySku(request.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        log.info("Product created with id: {}", savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "products", allEntries = true)
    })
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productMapper.updateEntity(product, request);
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated with id: {}", id);

        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        productRepository.save(product);
        log.info("Product deactivated with id: {}", id);
    }

    @Transactional
    public void rateProduct(Long productId, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        product.addRating(rating);
        productRepository.save(product);
        log.info("Product {} rated with {} stars", productId, rating);
    }
}