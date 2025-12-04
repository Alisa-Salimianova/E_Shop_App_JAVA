package io.github.alisa_salimianova.eshop.config;

import io.github.alisa_salimianova.eshop.model.entity.Product;
import io.github.alisa_salimianova.eshop.model.entity.User;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.repository.ProductRepository;
import io.github.alisa_salimianova.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Initializing sample data...");

            // Create sample users
            if (userRepository.count() == 0) {
                User user1 = User.builder()
                        .name("Alisa Salimianova")
                        .email("alisa@example.com")
                        .registeredAt(LocalDateTime.now())
                        .build();

                User user2 = User.builder()
                        .name("John Doe")
                        .email("john@example.com")
                        .registeredAt(LocalDateTime.now())
                        .build();

                userRepository.save(user1);
                userRepository.save(user2);
                log.info("Created sample users");
            }

            // Create sample products
            if (productRepository.count() == 0) {
                Product product1 = Product.builder()
                        .name("iPhone 15 Pro")
                        .description("Latest Apple smartphone with A17 Pro chip")
                        .price(new BigDecimal("999.99"))
                        .category(Category.ELECTRONICS)
                        .manufacturer("Apple")
                        .sku("IPHONE-15-PRO")
                        .stockQuantity(50)
                        .rating(4.8)
                        .ratingCount(1200)
                        .active(true)
                        .build();

                Product product2 = Product.builder()
                        .name("MacBook Air M2")
                        .description("Apple laptop with M2 chip, 13-inch")
                        .price(new BigDecimal("1199.99"))
                        .category(Category.ELECTRONICS)
                        .manufacturer("Apple")
                        .sku("MBA-M2-13")
                        .stockQuantity(30)
                        .rating(4.7)
                        .ratingCount(850)
                        .active(true)
                        .build();

                Product product3 = Product.builder()
                        .name("Design Patterns Book")
                        .description("Gang of Four design patterns book")
                        .price(new BigDecimal("49.99"))
                        .category(Category.BOOKS)
                        .manufacturer("Addison-Wesley")
                        .sku("DP-BOOK-1")
                        .stockQuantity(100)
                        .rating(4.9)
                        .ratingCount(1500)
                        .active(true)
                        .build();

                Product product4 = Product.builder()
                        .name("Nike Air Max")
                        .description("Running shoes with air cushioning")
                        .price(new BigDecimal("129.99"))
                        .category(Category.SPORTS)
                        .manufacturer("Nike")
                        .sku("NIKE-AIRMAX-270")
                        .stockQuantity(200)
                        .rating(4.5)
                        .ratingCount(2300)
                        .active(true)
                        .build();

                productRepository.save(product1);
                productRepository.save(product2);
                productRepository.save(product3);
                productRepository.save(product4);
                log.info("Created sample products");
            }

            log.info("Data initialization completed");
        };
    }
}