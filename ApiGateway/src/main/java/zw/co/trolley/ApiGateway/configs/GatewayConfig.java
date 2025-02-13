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
                        .path("/api/products/**", "/api/categories/**")
                        .uri("http://localhost:8082"))

                // Order Service Routes
                .route("order-service", r -> r
                        .path("/api/orders/**", "/api/cart/**")
                        .uri("http://localhost:8083"))

                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("http://localhost:8085"))

                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .uri("http://localhost:8086"))

                // Expose OpenAPI docs for Auth Service
                .route("openapi-auth-service", r -> r
                        .path("/v3/api-docs/auth/**")
                        .filters(f -> f.stripPrefix(2)) // Remove /v3/api-docs
                        .uri("http://localhost:8081"))

                // Expose OpenAPI docs for Product Service
                .route("openapi-product-service", r -> r
                        .path("/v3/api-docs/product/**")
                        .filters(f -> f.stripPrefix(2)) // Remove /v3/api-docs
                        .uri("http://localhost:8082"))

                // Expose OpenAPI docs for Order Service
                .route("openapi-order-service", r -> r
                        .path("/v3/api-docs/order/**")
                        .filters(f -> f.stripPrefix(2)) // Remove /v3/api-docs
                        .uri("http://localhost:8083"))

                // Expose OpenAPI docs for Notification Service
                .route("openapi-notification-service", r -> r
                        .path("/v3/api-docs/notification/**")
                        .filters(f -> f.stripPrefix(2)) // Remove /v3/api-docs
                        .uri("http://localhost:8085"))

                // Expose OpenAPI docs for Payment Service
                .route("openapi-payment-service", r -> r
                        .path("/v3/api-docs/payment/**")
                        .filters(f -> f.stripPrefix(2)) // Remove /v3/api-docs
                        .uri("http://localhost:8086"))
                .build();
    }
}
