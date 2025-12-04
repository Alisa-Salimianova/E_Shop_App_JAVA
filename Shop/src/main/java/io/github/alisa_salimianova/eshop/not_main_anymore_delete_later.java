package io.github.alisa_salimianova.eshop;

import io.github.alisa_salimianova.eshop.model.entity.*;
import io.github.alisa_salimianova.eshop.model.enums.Category;
import io.github.alisa_salimianova.eshop.repository.*;
import io.github.alisa_salimianova.eshop.service.*;
import io.github.alisa_salimianova.eshop.service.interfaces.*;
import io.github.alisa_salimianova.eshop.strategy.delivery.ExpressDelivery;
import io.github.alisa_salimianova.eshop.strategy.delivery.StandardDelivery;
import io.github.alisa_salimianova.eshop.strategy.filter.CategoryFilterStrategy;
import io.github.alisa_salimianova.eshop.strategy.filter.PriceFilterStrategy;
import io.github.alisa_salimianova.eshop.strategy.payment.CreditCardPayment;
import io.github.alisa_salimianova.eshop.strategy.payment.PayPalPayment;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static ProductRepository productRepository;
    private static UserRepository userRepository;
    private static OrderRepository orderRepository;
    private static ProductService productService;
    private static OrderService orderService;
    private static RecommendationService recommendationService;
    private static User currentUser;

    public static void main(String[] args) {
        System.out.println("=== ДОБРО ПОЖАЛОВАТЬ В E-SHOP ===");
        System.out.println("Магазин электронной коммерции\n");

        initializeServices();
        showMainMenu();
    }

    private static void initializeServices() {
        productRepository = new ProductRepository();
        userRepository = new UserRepository();
        orderRepository = new OrderRepository();
        productService = new ProductService(productRepository);
        orderService = new OrderService(orderRepository, userRepository);
        recommendationService = new RecommendationService(productService, orderService);

        // Создаем тестового пользователя (первого в списке)
        currentUser = userRepository.findAll().get(0);
        System.out.println("Вы вошли как: " + currentUser);
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. 📦 Просмотр товаров");
            System.out.println("2. 🔍 Фильтрация товаров");
            System.out.println("3. 🛒 Корзина");
            System.out.println("4. 📋 Мои заказы");
            System.out.println("5. 💡 Рекомендации");
            System.out.println("6. 👤 Сменить пользователя");
            System.out.println("0. ❌ Выход");
            System.out.print("Выберите действие: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> showProducts();
                case 2 -> filterProducts();
                case 3 -> showCart();
                case 4 -> showOrders();
                case 5 -> showRecommendations();
                case 6 -> changeUser();
                case 0 -> {
                    System.out.println("До свидания! Спасибо за посещение нашего магазина!");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void showProducts() {
        List<Product> products = productService.getAllProducts();
        System.out.println("\n=== ВСЕ ТОВАРЫ ===");

        if (products.isEmpty()) {
            System.out.println("Товары отсутствуют.");
            return;
        }

        products.forEach(System.out::println);

        System.out.print("\nДобавить товар в корзину (введите ID) или 0 для возврата: ");
        int productId = getIntInput();

        if (productId != 0) {
            try {
                Product product = productService.getProductById(productId);
                currentUser.addToCart(product);
                System.out.println("✅ Товар добавлен в корзину: " + product.getName());

                System.out.print("Оценить товар (1-5) или 0 чтобы пропустить: ");
                int rating = getIntInput();
                if (rating >= 1 && rating <= 5) {
                    productService.rateProduct(productId, rating);
                    System.out.println("⭐ Спасибо за оценку!");
                }
            } catch (Exception e) {
                System.out.println("❌ Ошибка: " + e.getMessage());
            }
        }
    }

    private static void filterProducts() {
        System.out.println("\n=== ФИЛЬТРАЦИЯ ТОВАРОВ ===");
        System.out.println("1. По цене (максимальная цена)");
        System.out.println("2. По категории");
        System.out.print("Выберите тип фильтра: ");

        int choice = getIntInput();
        scanner.nextLine(); // Очистка буфера

        FilterStrategy strategy;
        String criteria;

        switch (choice) {
            case 1 -> {
                System.out.print("Введите максимальную цену: ");
                criteria = scanner.nextLine();
                strategy = new PriceFilterStrategy();
            }
            case 2 -> {
                System.out.println("Доступные категории:");
                for (Category category : Category.values()) {
                    System.out.println("- " + category.name() + " (" + category.getDisplayName() + ")");
                }
                System.out.print("Введите название категории: ");
                criteria = scanner.nextLine().toUpperCase();
                strategy = new CategoryFilterStrategy();
            }
            default -> {
                System.out.println("Неверный выбор");
                return;
            }
        }

        try {
            List<Product> filteredProducts = productService.filterProducts(strategy, criteria);
            System.out.println("\nРезультаты фильтрации:");

            if (filteredProducts.isEmpty()) {
                System.out.println("Товары не найдены.");
            } else {
                filteredProducts.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка фильтрации: " + e.getMessage());
        }
    }

    private static void showCart() {
        List<Product> cart = currentUser.getCart();
        System.out.println("\n=== КОРЗИНА ===");

        if (cart.isEmpty()) {
            System.out.println("Корзина пуста");
            return;
        }

        cart.forEach(System.out::println);
        System.out.printf("Итого: $%.2f%n", currentUser.getCartTotal());

        System.out.println("\n1. Оформить заказ");
        System.out.println("2. Очистить корзину");
        System.out.println("3. Удалить товар из корзины");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> createOrder();
            case 2 -> {
                currentUser.clearCart();
                System.out.println("✅ Корзина очищена");
            }
            case 3 -> removeFromCart();
        }
    }

    private static void removeFromCart() {
        System.out.print("Введите ID товара для удаления: ");
        int productId = getIntInput();

        List<Product> cart = currentUser.getCart();
        Product toRemove = cart.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            currentUser.removeFromCart(toRemove);
            System.out.println("✅ Товар удален из корзины");
        } else {
            System.out.println("❌ Товар с таким ID не найден в корзине");
        }
    }

    private static void createOrder() {
        System.out.println("\n=== ОФОРМЛЕНИЕ ЗАКАЗА ===");

        // Выбор способа оплаты
        System.out.println("Способы оплаты:");
        System.out.println("1. 💳 Кредитная карта");
        System.out.println("2. 🔵 PayPal");
        System.out.print("Выберите способ оплаты: ");
        int paymentChoice = getIntInput();

        PaymentStrategy paymentStrategy = switch (paymentChoice) {
            case 1 -> new CreditCardPayment();
            case 2 -> new PayPalPayment();
            default -> {
                System.out.println("Неверный выбор, используется кредитная карта");
                yield new CreditCardPayment();
            }
        };

        // Выбор способа доставки
        System.out.println("Способы доставки:");
        System.out.println("1. 🚚 Стандартная доставка (5 дней)");
        System.out.println("2. ⚡ Экспресс-доставка (1 день)");
        System.out.print("Выберите способ доставки: ");
        int deliveryChoice = getIntInput();

        DeliveryStrategy deliveryStrategy = switch (deliveryChoice) {
            case 1 -> new StandardDelivery();
            case 2 -> new ExpressDelivery();
            default -> {
                System.out.println("Неверный выбор, используется стандартная доставка");
                yield new StandardDelivery();
            }
        };

        try {
            orderService.createOrder(currentUser.getId(), paymentStrategy, deliveryStrategy);
        } catch (Exception e) {
            System.out.println("❌ Ошибка создания заказа: " + e.getMessage());
        }
    }

    private static void showOrders() {
        List<Order> orders = orderService.getUserOrders(currentUser.getId());
        System.out.println("\n=== МОИ ЗАКАЗЫ ===");

        if (orders.isEmpty()) {
            System.out.println("У вас пока нет заказов.");
            return;
        }

        orders.forEach(System.out::println);

        System.out.println("\n1. 🔄 Повторить заказ");
        System.out.println("2. ❌ Отменить заказ");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");

        int choice = getIntInput();
        if (choice == 1 || choice == 2) {
            System.out.print("Введите номер заказа: ");
            int orderId = getIntInput();

            try {
                if (choice == 1) {
                    orderService.repeatOrder(orderId, currentUser.getId());
                    System.out.println("✅ Товары из заказа добавлены в корзину");
                } else {
                    orderService.cancelOrder(orderId, currentUser.getId());
                }
            } catch (Exception e) {
                System.out.println("❌ Ошибка: " + e.getMessage());
            }
        }
    }

    private static void showRecommendations() {
        System.out.println("\n=== РЕКОМЕНДАЦИИ ===");

        System.out.println("⭐ Самые популярные товары:");
        List<Product> topRated = recommendationService.getTopRatedProducts();
        if (topRated.isEmpty()) {
            System.out.println("Пока нет оцененных товаров.");
        } else {
            topRated.forEach(System.out::println);
        }

        System.out.println("\n🎯 Рекомендуем вам:");
        List<Product> recommendations = recommendationService.getRecommendationsForUser(currentUser.getId());
        if (recommendations.isEmpty()) {
            System.out.println("Сделайте первые покупки, чтобы получить рекомендации!");
        } else {
            recommendations.forEach(System.out::println);
        }
    }

    private static void changeUser() {
        System.out.println("\n=== СМЕНА ПОЛЬЗОВАТЕЛЯ ===");
        List<User> users = userRepository.findAll();

        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }

        System.out.print("Выберите пользователя: ");
        int choice = getIntInput();

        if (choice > 0 && choice <= users.size()) {
            currentUser = users.get(choice - 1);
            System.out.println("✅ Теперь вы: " + currentUser);
        } else {
            System.out.println("❌ Неверный выбор");
        }
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Пожалуйста, введите число: ");
            }
        }
    }
}