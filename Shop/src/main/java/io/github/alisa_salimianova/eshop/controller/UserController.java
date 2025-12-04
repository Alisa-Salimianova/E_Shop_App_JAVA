package io.github.alisa_salimianova.eshop.controller;

import io.github.alisa_salimianova.eshop.dto.response.ApiResponseDto;
import io.github.alisa_salimianova.eshop.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}/recommendations")
    @Operation(summary = "Get product recommendations for user")
    public ResponseEntity<ApiResponseDto<?>> getUserRecommendations(@PathVariable Long userId) {
        var recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(ApiResponseDto.success(recommendations));
    }

    @GetMapping("/recommendations/top-rated")
    @Operation(summary = "Get top rated products")
    public ResponseEntity<ApiResponseDto<?>> getTopRatedProducts() {
        var topRated = recommendationService.getTopRatedProducts();
        return ResponseEntity.ok(ApiResponseDto.success(topRated));
    }
}