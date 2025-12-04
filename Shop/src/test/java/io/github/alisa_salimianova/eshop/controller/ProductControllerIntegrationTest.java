package io.github.alisa_salimianova.eshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alisa_salimianova.eshop.dto.request.CreateProductRequest;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_shouldReturnCreatedStatus() throws Exception {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Integration Test Product");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("49.99"));
        request.setCategory(Category.ELECTRONICS);
        request.setSku("INT-TEST-001");
        request.setStockQuantity(100);
        request.setManufacturer("Test Manufacturer");

        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"));
    }

    @Test
    void createProduct_shouldReturnBadRequest_whenInvalidData() throws Exception {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName(""); // Empty name - invalid
        request.setPrice(new BigDecimal("-10.00")); // Negative price - invalid

        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}