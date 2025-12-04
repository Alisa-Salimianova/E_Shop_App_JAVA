package io.github.alisa_salimianova.eshop.controller;

import io.github.alisa_salimianova.eshop.dto.request.CreateOrderRequest;
import io.github.alisa_salimianova.eshop.dto.response.ApiResponseDto;
import io.github.alisa_salimianova.eshop.dto.response.OrderResponse;
import io.github.alisa_salimianova.eshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        // Создаем заглушку для теста
        OrderResponse order = OrderResponse.builder()
                .id(1L)
                .userId(request.getUserId())
                .userName("Test User")
                .userEmail("test@example.com")
                .totalAmount(calculateOrderTotal(request))
                .status(io.github.alisa_salimianova.eshop.model.enums.OrderStatus.PROCESSING)
                .orderDate(LocalDateTime.now())
                .paymentMethod("Credit Card")
                .deliveryMethod("Standard Delivery")
                .shippingAddress(request.getShippingAddress())
                .build();

        return ResponseEntity.ok(ApiResponseDto.success(order, "Order created successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<OrderResponse>>> getUserOrders(
            @PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponseDto.success(orders));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(ApiResponseDto.success(null, "Order cancelled successfully"));
    }

    private BigDecimal calculateOrderTotal(CreateOrderRequest request) {
        // Простая логика расчета - 100$ за каждый товар
        return BigDecimal.valueOf(request.getItems().size() * 100.0);
    }
}