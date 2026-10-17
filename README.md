# E-Commerce Microservices

A set of independently built Spring Boot applications. The repository root project is the Config Server; each service directory has its own Gradle build and can be started or tested independently.

## Applications and local ports

| Application | Directory | Port | Local dependency |
|---|---|---:|---|
| Config Server | repository root | 8888 | Config repository configured in `src/main/resources/application.yaml` |
| Eureka Server | `eurekaserver` | 8761 | none |
| User Service | `user-service` | 8081 | PostgreSQL database `ecommerce` |
| Product Service | `product-service` | 8082 | PostgreSQL database `product_db`, RabbitMQ |
| Inventory Service | `inventory-service` | 8083 | PostgreSQL database `inventory_db`, RabbitMQ |
| Cart Service | `cart-service` | 8084 | Redis |
| Order Service | `order-service` | 8085 | PostgreSQL database `order_db`, RabbitMQ |
| Review Service | `review-service` | 8086 | PostgreSQL database `review_db` |
| AI Service | `ai-service` | 8087 | Product, Order, and Review Services; AI provider when AI endpoints are used |

## Run applications separately in IntelliJ

Open the root directory as the IntelliJ project. Each service is a separate Gradle project. Open the `*Application` class under that service's `src/main/java` directory and run it as a Spring Boot application. Alternatively, open a terminal in that service directory and run:

```bash
./gradlew bootRun
```

On Windows, use `gradlew.bat bootRun`. Start the Config Server and Eureka Server before the services that register with Eureka or import remote configuration. Start PostgreSQL, RabbitMQ, and Redis before the services that use them. The services remain independently runnable; no single command starts the whole system.

Local PostgreSQL credentials are configured in the application YAML files for the database-backed services. JWT signing keys, the AI provider key, and internal service keys are still supplied through each service's IntelliJ run configuration because they are separate secrets.

## Run tests

Tests use a separate `test` profile. JPA services use an in-memory H2 database, and tests disable remote Config Server, Eureka, and RabbitMQ connections where applicable. They do not read or modify local PostgreSQL data.

Run one service's tests from that directory:

```bash
cd user-service
./gradlew test
```

Run the Config Server test from the repository root with `./gradlew test`. To run all tests on macOS/Linux from the root:

```bash
for service in ai-service cart-service eurekaserver inventory-service order-service product-service review-service user-service; do
  (cd "$service" && ./gradlew test) || exit 1
done
./gradlew test
```

## Project layout

```text
.
├── src/                 # Config Server
├── ai-service/
├── cart-service/
├── eurekaserver/
├── inventory-service/
├── order-service/
├── product-service/
├── review-service/
└── user-service/
```

Each service keeps its application source, resources, tests, and Gradle configuration under its own directory. The independent Gradle builds are intentional because services are run separately.
