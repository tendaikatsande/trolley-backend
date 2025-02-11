package zw.co.trolley.ApiGateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8081"))

                // Product Service Routes
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .uri("http://localhost:8082"))

                // Order Service Routes
                .route("order-service", r -> r
                        .path("/api/orders/**", "/api/cart/**")
                        .uri("http://localhost:8083"))

                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("http://localhost:8084"))
                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("http://localhost:8085"))
                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .uri("http://localhost:8086"))

                // Swagger UI for API Gateway
                .route("swagger-api-gateway", r -> r
                        .path("/swagger-ui/api-gateway/**")
                        .uri("http://localhost:8080/swagger-ui/index.html"))

                // Swagger UI for Auth Service
                .route("swagger-auth-service", r -> r
                        .path("/swagger-ui/auth/**")
                        .uri("http://localhost:8081/swagger-ui/index.html"))

                // Swagger UI for Product Service
                .route("swagger-product-service", r -> r
                        .path("/swagger-ui/product/**")
                        .uri("http://localhost:8082/swagger-ui/index.html"))

                // Swagger UI for Order Service
                .route("swagger-order-service", r -> r
                        .path("/swagger-ui/order/**")
                        .uri("http://localhost:8083/swagger-ui/index.html"))

                // Swagger UI for User Service
                .route("swagger-user-service", r -> r
                        .path("/swagger-ui/user/**")
                        .uri("http://localhost:8084/swagger-ui/index.html"))
                // Swagger UI for Notification Service
                .route("swagger-notification-service", r -> r
                        .path("/swagger-ui/notification/**")
                        .uri("http://localhost:8085/swagger-ui/index.html"))

                // Swagger UI for Payment Service
                .route("swagger-payment-service", r -> r
                        .path("/swagger-ui/payment/**")
                        .uri("http://localhost:8086/swagger-ui/index.html"))


                // Expose OpenAPI docs for Auth Service
                .route("openapi-auth-service", r -> r
                        .path("/v3/api-docs/auth/**")
                        .uri("http://localhost:8081/v3/api-docs"))

                // Expose OpenAPI docs for Product Service
                .route("openapi-product-service", r -> r
                        .path("/v3/api-docs/product/**")
                        .uri("http://localhost:8082/v3/api-docs"))

                // Expose OpenAPI docs for Order Service
                .route("openapi-order-service", r -> r
                        .path("/v3/api-docs/order/**")
                        .uri("http://localhost:8083/v3/api-docs"))

                // Expose OpenAPI docs for User Service
                .route("openapi-user-service", r -> r
                        .path("/v3/api-docs/user/**")
                        .uri("http://localhost:8084/v3/api-docs"))
                // Expose OpenAPI docs for Notification Service
                .route("openapi-notification-service", r -> r
                        .path("/v3/api-docs/notification/**")
                        .uri("http://localhost:8085/v3/api-docs"))
                // Expose OpenAPI docs for Payment Service
                .route("openapi-payment-service", r -> r
                        .path("/v3/api-docs/payment/**")
                        .uri("http://localhost:8086/v3/api-docs"))
                .build();
    }
}
