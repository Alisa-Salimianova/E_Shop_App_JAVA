package io.github.alisa_salimianova.eshop.controller;

import io.github.alisa_salimianova.eshop.dto.request.CreateProductRequest;
import io.github.alisa_salimianova.eshop.dto.request.UpdateProductRequest;
import io.github.alisa_salimianova.eshop.dto.response.ApiResponseDto;
import io.github.alisa_salimianova.eshop.dto.response.ProductResponse;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all active products")
    public ResponseEntity<ApiResponseDto<List<ProductResponse>>> getAllActiveProducts() {
        List<ProductResponse> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(ApiResponseDto.success(products));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponseDto<ProductResponse>> getProductById(
            @PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponseDto.success(product));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category with pagination")
    public ResponseEntity<ApiResponseDto<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Category category,
            @Parameter(description = "Pagination parameters")
            Pageable pageable) {
        Page<ProductResponse> products = productService.getProductsByCategory(category, pageable);
        return ResponseEntity.ok(ApiResponseDto.success(products));
    }

    @GetMapping("/price/under")
    @Operation(summary = "Get products under specified price")
    public ResponseEntity<ApiResponseDto<List<ProductResponse>>> getProductsUnderPrice(
            @RequestParam @Min(0) @Max(10000) BigDecimal maxPrice) {
        List<ProductResponse> products = productService.getProductsUnderPrice(maxPrice);
        return ResponseEntity.ok(ApiResponseDto.success(products));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseDto<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(product, "Product created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponseDto<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponseDto.success(product, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product (soft delete)")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseDto.success(null, "Product deleted successfully"));
    }

    @PostMapping("/{id}/rate")
    @Operation(summary = "Rate a product")
    public ResponseEntity<ApiResponseDto<Void>> rateProduct(
            @PathVariable Long id,
            @RequestParam @Min(1) @Max(5) Integer rating) {
        productService.rateProduct(id, rating);
        return ResponseEntity.ok(ApiResponseDto.success(null, "Rating submitted successfully"));
    }
}