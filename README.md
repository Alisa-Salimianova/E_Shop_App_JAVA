# 🛒 Modern E-Shop Application

A production-ready e-commerce backend application built with Spring Boot, demonstrating modern Java development practices and SOLID principles.

## 🚀 Features

- **RESTful API** with proper HTTP methods and status codes
- **JPA/Hibernate** for database operations with PostgreSQL
- **Validation** using Jakarta Bean Validation
- **Global Exception Handling** with custom error responses
- **Caching** with Spring Cache (Redis)
- **Pagination & Sorting** for list endpoints
- **Strategy Pattern** for payment and delivery methods
- **Comprehensive Testing** (Unit & Integration with Testcontainers)
- **API Documentation** with Swagger/OpenAPI 3
- **Docker Support** for containerization with Docker Compose
- **Monitoring** with Prometheus and Grafana
- **Logging** with SLF4J & Logback
- **Health Checks** with Spring Boot Actuator

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **PostgreSQL & H2** (for testing)
- **Redis** for caching
- **Lombok** for boilerplate code reduction
- **MapStruct** for object mapping
- **Spring Cache**
- **Swagger/OpenAPI 3** for API documentation
- **Testcontainers** for integration testing
- **JUnit 5 & Mockito** for testing
- **Docker & Docker Compose** for containerization
- **Prometheus & Grafana** for monitoring

## 📁 Project Structure

```
e-shop-spring/
├── src/main/java/io/github/alisa_salimianova/eshop/
│   ├── EshopApplication.java          # Main application class
│   ├── config/                        # Configuration classes
│   ├── controller/                    # REST controllers
│   ├── dto/                           # Data Transfer Objects
│   │   ├── request/                   # Request DTOs
│   │   └── response/                  # Response DTOs
│   ├── exception/                     # Custom exceptions
│   ├── mapper/                        # MapStruct mappers
│   ├── model/                         # Domain models
│   │   ├── entity/                    # JPA entities
│   │   └── enums/                     # Enumerations
│   ├── repository/                    # Spring Data repositories
│   ├── service/                       # Business logic layer
│   └── strategy/                      # Strategy pattern implementations
│       ├── delivery/                  # Delivery strategies
│       └── payment/                   # Payment strategies
├── src/main/resources/
│   ├── application.yml                # Main configuration
│   ├── application-prod.yml           # Production configuration
│   └── application-test.yml           # Test configuration
├── src/test/                          # Tests
├── Dockerfile                         # Docker build file
├── docker-compose.yml                 # Docker Compose setup
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

## 🏗️ SOLID Principles Implementation

### 1. **Single Responsibility Principle (SRP)**
- `ProductService` - manages only product-related operations
- `OrderService` - handles only order processing
- Each controller has a single responsibility

**Пример кода:** `ProductService.java` отвечает только за бизнес-логику товаров

### 2. **Open/Closed Principle (OCP)**
- `FilterStrategy` interface allows adding new filters without modifying existing code
- `PaymentStrategy` and `DeliveryStrategy` can be extended with new implementations

**Пример кода:** Новый `ManufacturerFilterStrategy` можно добавить без изменения `ProductService`

### 3. **Liskov Substitution Principle (LSP)**
- All strategy implementations can be used interchangeably through their interfaces
- Repository implementations follow Spring Data JPA contract

**Пример кода:** `CreditCardPaymentStrategy` и `PayPalPaymentStrategy` взаимозаменяемы через `PaymentStrategy`

### 4. **Interface Segregation Principle (ISP)**
- Separate interfaces for `PaymentStrategy`, `DeliveryStrategy`, `FilterStrategy`
- Each interface contains only relevant methods

**Пример кода:** `PaymentStrategy` содержит только методы оплаты, `DeliveryStrategy` - только доставки

### 5. **Dependency Inversion Principle (DIP)**
- High-level modules depend on abstractions (interfaces)
- Dependency injection through Spring framework

**Пример кода:** `OrderService` зависит от `PaymentStrategy`, а не от конкретных реализаций

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (optional)
- PostgreSQL (for production)

### Running Locally

1. **Clone and build:**
```bash
git clone https://github.com/alisa_salimianova/e-shop.git
cd e-shop
mvn clean install
```

2. **Run with H2 database:**
```bash
mvn spring-boot:run
```

3. **Access the application:**
- API: http://localhost:8080/api/v1/
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
- Actuator Health: http://localhost:8080/actuator/health

### Running with Docker Compose

1. **Start all services:**
```bash
docker-compose up --build
```

2. **Access services:**
- Application: http://localhost:8080
- PostgreSQL: localhost:5432
- Redis: localhost:6379
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

## 📚 API Documentation

### Products Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/products` | Get all active products |
| GET | `/api/v1/products/{id}` | Get product by ID |
| POST | `/api/v1/products` | Create new product |
| PUT | `/api/v1/products/{id}` | Update product |
| DELETE | `/api/v1/products/{id}` | Delete product (soft delete) |
| POST | `/api/v1/products/{id}/rate` | Rate a product (1-5) |
| GET | `/api/v1/products/category/{category}` | Get products by category |
| GET | `/api/v1/products/price/under` | Get products under price |

### Orders Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/orders` | Create order |
| GET | `/api/v1/orders/user/{userId}` | Get user's orders |
| POST | `/api/v1/orders/{orderId}/cancel` | Cancel order |

### Users Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/{userId}/recommendations` | Get recommendations |
| GET | `/api/v1/users/recommendations/top-rated` | Get top rated products |

## 🧪 Testing

### Run Tests
```bash
# Unit tests
mvn test

# Integration tests
mvn verify -Pintegration

# All tests
mvn clean verify

# With coverage report
mvn jacoco:report
```

### Test Examples

**Unit Test:**
```java
@Test
void getProductById_shouldReturnProduct_whenExists() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    ProductResponse result = productService.getProductById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
}
```

**Integration Test:**
```java
@Test
void createProduct_shouldReturnCreatedStatus() throws Exception {
    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true));
}
```

## 📊 Monitoring & Observability

### Health Checks
Spring Boot Actuator provides comprehensive health checks:
- Application health status
- Database connectivity
- Disk space
- Custom health indicators

**Endpoints:**
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics
- `/actuator/info` - Application info

### Logging
- Structured logging with Logback
- JSON format in production
- Log rotation with size/time limits
- Correlation IDs for request tracing

**Logback configuration:**
```xml
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/eshop-app.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>logs/eshop-app-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxHistory>30</maxHistory>
    </rollingPolicy>
</appender>
```

## 🔧 Configuration

### Profiles
| Profile | Database | Purpose |
|---------|----------|---------|
| `dev` | H2 (in-memory) | Development |
| `test` | H2/Testcontainers | Testing |
| `prod` | PostgreSQL + Redis | Production |

### Environment Variables
```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/eshopdb
SPRING_DATASOURCE_USERNAME=eshopuser
SPRING_DATASOURCE_PASSWORD=eshoppass

# Redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# Application
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

## 🐳 Docker Deployment

### Build and Run
```bash
# Build Docker image
docker build -t eshop-app:latest .

# Run with Docker Compose
docker-compose up --build

# Run in background
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down
```

### Docker Services Architecture
```
┌─────────────────┐    ┌─────────────┐    ┌─────────────┐
│   E-Shop App    │    │  PostgreSQL │    │    Redis    │
│   (Spring Boot) │◄──►│   Database  │◄──►│   Cache     │
└─────────────────┘    └─────────────┘    └─────────────┘
         │                      │                    │
         ▼                      ▼                    ▼
┌─────────────────┐    ┌─────────────┐    ┌─────────────┐
│   Prometheus    │    │   Grafana   │    │   NGINX     │
│   Monitoring    │◄──►│   Dashboard │◄──►│   Reverse   │
│                 │    │             │    │   Proxy     │
└─────────────────┘    └─────────────┘    └─────────────┘
```

## 📈 Performance Optimization

### Caching Strategy
```java
@Cacheable(value = "products", key = "#id")
public ProductResponse getProductById(Long id) {
    // Cache miss - fetch from database
}

@CacheEvict(value = "products", key = "#id")
public void updateProduct(Long id) {
    // Update database and evict cache
}
```

### Database Optimization
- Indexes on frequently queried columns (category, price, rating)
- Connection pooling with HikariCP
- Batch processing for bulk operations
- Read replicas for scaling

## 🔒 Security Considerations

### Implemented
- Input validation with Jakarta Bean Validation
- SQL injection prevention with JPA
- XSS protection with Spring Security
- HTTPS enforcement in production
- CORS configuration

### To Implement (Recommended)
- JWT-based authentication
- OAuth2/OpenID Connect
- Rate limiting with Redis
- API key management
- Audit logging
- RBAC (Role-Based Access Control)

## 🚀 Deployment Strategies

### 1. **Traditional Deployment**
```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/e-shop-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/eshopdb
```

### 2. **Docker Deployment**
```bash
# Build and push to registry
docker build -t your-registry/eshop-app:1.0.0 .
docker push your-registry/eshop-app:1.0.0

# Deploy to server
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  your-registry/eshop-app:1.0.0
```

### 3. **Kubernetes Deployment**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eshop-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: eshop
  template:
    metadata:
      labels:
        app: eshop
    spec:
      containers:
      - name: eshop-app
        image: your-registry/eshop-app:1.0.0
        ports:
        - containerPort: 8080
```

## 📊 Monitoring Dashboard

### Grafana Dashboards Included
1. **Application Metrics**
    - Request rate and latency
    - Error rates
    - JVM metrics (memory, GC, threads)

2. **Business Metrics**
    - Orders per hour
    - Revenue metrics
    - Popular products
    - User engagement

3. **Infrastructure Metrics**
    - CPU/Memory usage
    - Database connections
    - Cache hit rates

## 🔄 CI/CD Pipeline

### GitHub Actions Example
```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Run tests
        run: mvn clean verify
        
  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build Docker image
        run: docker build -t eshop-app:${{ github.sha }} .
        
  deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to production
        run: |
          docker-compose -f docker-compose.prod.yml up -d
```

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### Development Guidelines
- Follow SOLID principles
- Write unit tests for new features
- Update API documentation
- Maintain code coverage above 80%
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot Team** - For the amazing framework that makes Java development enjoyable
- **Testcontainers** - For making integration testing with real databases painless
- **OpenAPI/Swagger** - For excellent API documentation tools
- **PostgreSQL, Redis, and Docker Communities** - For robust infrastructure tools
- **All Contributors** - Who help improve this project

## 📞 Contact & Support

**Alisa Salimianova**
- GitHub: [@alisa_salimianova](https://github.com/alisa_salimianova)
- Email: alisa@example.com

**Project Links**
- Repository: https://github.com/alisa_salimianova/e-shop
- Issue Tracker: https://github.com/alisa_salimianova/e-shop/issues
- Wiki: https://github.com/alisa_salimianova/e-shop/wiki

## 🎯 Roadmap

### Short-term (v1.1)
- [ ] Add authentication with JWT
- [ ] Implement shopping cart functionality
- [ ] Add email notifications
- [ ] Implement payment gateway integration

### Medium-term (v1.2)
- [ ] Add Elasticsearch for product search
- [ ] Implement recommendation engine with ML
- [ ] Add multi-language support
- [ ] Implement WebSocket for real-time updates

### Long-term (v2.0)
- [ ] Microservices architecture
- [ ] Event-driven architecture with Kafka
- [ ] GraphQL API
- [ ] Mobile app with React Native

---

**⭐ If you find this project useful, please give it a star on GitHub! ⭐**

## 📚 Additional Resources

### Learning Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [SOLID Principles Explained](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Docker for Java Developers](https://docs.docker.com/language/java/)
- [REST API Best Practices](https://restfulapi.net/)

### Tools Used
- **IntelliJ IDEA** - Primary IDE
- **Postman** - API testing
- **DBeaver** - Database management
- **Visual Studio Code** - Markdown editing

### Similar Projects
- [Spring PetClinic](https://github.com/spring-projects/spring-petclinic)
- [Microservices with Spring Cloud](https://spring.io/microservices)
- [JHipster](https://www.jhipster.tech/)

---

*This project was developed as a portfolio piece to demonstrate modern Java development practices and is actively maintained.*
