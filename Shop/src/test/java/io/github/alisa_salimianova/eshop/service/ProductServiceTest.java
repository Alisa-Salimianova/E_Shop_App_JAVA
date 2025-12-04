package io.github.alisa_salimianova.eshop.service;

import io.github.alisa_salimianova.eshop.dto.request.CreateProductRequest;
import io.github.alisa_salimianova.eshop.dto.response.ProductResponse;
import io.github.alisa_salimianova.eshop.exception.ResourceNotFoundException;
import io.github.alisa_salimianova.eshop.mapper.ProductMapper;
import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductResponse testProductResponse;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .category(Category.ELECTRONICS)
                .sku("TEST-001")
                .stockQuantity(10)
                .rating(4.5)
                .ratingCount(100)
                .active(true)
                .build();

        testProductResponse = ProductResponse.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .category(Category.ELECTRONICS)
                .build();
    }

    @Test
    void getProductById_shouldReturnProduct_whenExists() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

        // Act
        ProductResponse result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_shouldThrowException_whenNotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(999L);
        });
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("New Product");
        request.setDescription("New Description");
        request.setPrice(new BigDecimal("199.99"));
        request.setCategory(Category.CLOTHING);
        request.setSku("NEW-001");
        request.setStockQuantity(50);
        request.setManufacturer("Test Manufacturer");

        when(productRepository.findBySku("NEW-001")).thenReturn(Optional.empty());
        when(productMapper.toEntity(request)).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

        // Act
        ProductResponse result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}